package com.gui.jhand.hand;

import com.gui.jhand.TestBase;
import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.pokersession.CashPokerSession;
import com.gui.jhand.pokersession.PokerSessionService;
import com.gui.jhand.pokersession.TournamentPokerSession;
import com.gui.jhand.pokersession.TournamentResult;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.time.Instant;
import java.util.List;

import static com.gui.jhand.hand.HandResultTemplateLoader.handWithNetProfitAt;
import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HandImportServiceTest extends TestBase {

	private static final String FILE_NAME = "HH20260224 T3978149677 No Limit Hold'em $0.90 + $0.10.txt";

	private static final String SESSION_ID = "HH20260224 T3978149677 No Limit Hold'em $0.90 + $0.10";

	@Mock
	private HandResultBuilder builder;

	@Mock
	private HandResultRepository repository;

	@Mock
	private PokerSessionService pokerSessionService;

	private HandImportService service;

	private String rawHands;

	@Override
	public void init() {
		service = new HandImportService(builder, repository, pokerSessionService);
		rawHands = HandTemplateLoader.heroAtBtn().concat("\n\n\n").concat(HandTemplateLoader.heroAtSB());
	}

	@Test
	void should_save_all_hand_results_from_file() {
		Instant firstHandTime = Instant.ofEpochSecond(1771774381);
		HandResult winningHand = handWithNetProfitAt("150.25", firstHandTime.plusSeconds(60));
		HandResult losingHand = handWithNetProfitAt("-50.10", firstHandTime);
		when(builder.build(anyList(), anyString(), any(), anyString())).thenReturn(winningHand, losingHand);

		assertThatCode(() -> service.saveAllHandResultsFromFile(rawHands, FILE_NAME, "GuiRodri2013"))
			.doesNotThrowAnyException();

		InOrder inOrder = inOrder(builder, repository, pokerSessionService);
		inOrder.verify(builder, times(2)).build(anyList(), eq(SESSION_ID), eq(TOURNAMENT), eq("GuiRodri2013"));
		inOrder.verify(repository).saveAll(List.of(winningHand, losingHand));
		inOrder.verify(pokerSessionService)
			.save(argThat(
					session -> session instanceof TournamentPokerSession && session.getSessionId().equals(SESSION_ID)
							&& session.getHandCount() == 2 && firstHandTime.equals(session.getStartTime())
							&& firstHandTime.plusSeconds(60).equals(session.getEndTime())));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_save_tournament_session_with_prize_minus_buy_in() {
		Instant handTime = Instant.ofEpochSecond(1771774381);
		when(builder.build(anyList(), anyString(), any(), anyString()))
			.thenReturn(handWithNetProfitAt("100.00", handTime));

		service.saveAllHandResultsFromFile(HandTemplateLoader.heroWinsTournament(), FILE_NAME, "GuiRodri2013");

		verify(pokerSessionService).save(TournamentPokerSession.of(SESSION_ID, 1, handTime, handTime,
				new TournamentResult(ChipsUtils.of(0.50), ChipsUtils.of(5.67), 1, true, true)));
	}

	@Test
	void should_save_cash_session_with_the_sum_of_hands_net_profit() {
		String cashFileName = "HH20260222 Ljuba IV - $0.01-$0.02 - USD No Limit Hold'em.txt";
		Instant firstHandTime = Instant.ofEpochSecond(1771774381);
		when(builder.build(anyList(), anyString(), any(), anyString())).thenReturn(
				handWithNetProfitAt("0.05", firstHandTime.plusSeconds(60)),
				handWithNetProfitAt("-0.02", firstHandTime));

		service.saveAllHandResultsFromFile(rawHands, cashFileName, "GuiRodri2013");

		verify(pokerSessionService).save(CashPokerSession.of("HH20260222 Ljuba IV - $0.01-$0.02 - USD No Limit Hold'em",
				2, firstHandTime, firstHandTime.plusSeconds(60), ChipsUtils.of("0.03"), ChipsUtils.of("0.02")));
	}

	@Test
	void should_do_nothing_if_no_hands_in_file() {
		rawHands = "";

		assertThatCode(() -> service.saveAllHandResultsFromFile(rawHands, FILE_NAME, "GuiRodri2013"))
			.doesNotThrowAnyException();

		verifyNoInteractions(builder, repository, pokerSessionService);
	}

	@Test
	void should_fail_before_parsing_when_file_name_has_no_session_type() {
		assertThatThrownBy(() -> service.saveAllHandResultsFromFile(rawHands, "My Session.txt", "GuiRodri2013"))
			.isInstanceOf(IllegalArgumentException.class);

		verifyNoInteractions(builder, repository, pokerSessionService);
	}

}
