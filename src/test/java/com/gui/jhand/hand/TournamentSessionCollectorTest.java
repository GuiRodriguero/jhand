package com.gui.jhand.hand;

import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.parser.PokerStarsParser;
import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.TournamentPokerSession;
import com.gui.jhand.pokersession.TournamentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static com.gui.jhand.hand.HandResultTemplateLoader.handWithNetProfitAt;
import static com.gui.jhand.hand.HandTemplateLoader.*;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentSessionCollectorTest {

	private static final String HERO = "GuiRodri2013";

	private final PokerStarsParser parser = new PokerStarsParser();

	private TournamentSessionCollector collector;

	@BeforeEach
	void setUp() {
		collector = TournamentSessionCollector.of(HERO);
	}

	@Test
	void should_instantiate() {
		assertThat(TournamentSessionCollector.of(HERO)).isNotNull();
	}

	@Test
	void should_resolve_hero_winning_the_tournament() {
		acceptHand(heroWinsTournament());

		assertThat(collector.toResult())
			.isEqualTo(new TournamentResult(ChipsUtils.of(0.50), ChipsUtils.of(5.67), 1, true, true));
	}

	@Test
	void should_ignore_finish_of_other_players() {
		collector = TournamentSessionCollector.of("villain1");

		acceptHand(heroWinsTournament());

		assertThat(collector.toResult())
			.isEqualTo(new TournamentResult(ChipsUtils.of(0.50), ChipsUtils.of(3.70), 2, true, true));
	}

	@Test
	void should_not_be_finished_without_hero_finish_line() {
		acceptHand(heroAtBtn());

		TournamentResult result = collector.toResult();

		assertThat(result.finished()).isFalse();
		assertThat(result.finishPosition()).isNull();
		assertThat(result.netProfit()).isEqualTo(ZERO);
	}

	@Test
	void should_keep_the_first_buy_in_and_the_hero_finish_across_hands() {
		acceptHand(heroWinsTournament().replace("$0.45+$0.05", "$0.90+$0.10")
			.replace("GuiRodri2013 wins the tournament and receives $5.67 - congratulations!", ""));
		acceptHand(heroWinsTournament());

		assertThat(collector.toResult())
			.isEqualTo(new TournamentResult(ChipsUtils.of(1.0), ChipsUtils.of(5.67), 1, true, true));
	}

	@Test
	void should_resolve_freeroll_with_zero_buy_in() {
		acceptHand(heroWithShortBigBlind());
		acceptHand("GuiRodri2013 wins the tournament - congratulations!");

		assertThat(collector.toResult()).isEqualTo(new TournamentResult(ZERO, ChipsUtils.of(0.0), 1, false, true));
	}

	@Test
	void should_resolve_play_money_tournament() {
		acceptHand(heroWinsTournament().replace("$0.45+$0.05 USD", "21250+3750")
			.replace("$5.67", "153000.00")
			.replace("$3.70", "114750.00"));

		assertThat(collector.toResult())
			.isEqualTo(new TournamentResult(ChipsUtils.of(25000.0), ChipsUtils.of(153000.0), 1, false, true));
	}

	@Test
	void should_be_real_money_when_only_the_prize_is_in_dollars() {
		acceptHand("GuiRodri2013 finished the tournament in 3rd place and received $1.00.");

		assertThat(collector.toResult()).isEqualTo(new TournamentResult(ZERO, ChipsUtils.of(1.0), 3, true, true));
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', quoteCharacter = '"', nullValues = "null",
			value = { "HH20260219 T3976713284 No Limit Hold'em $0.92 + $0.08.txt|1.00|3.00|1|true",
					"HH20260227 T3979068754 No Limit Hold'em $0.90 + $0.10.txt|1.00|7.40|2|true",
					"HH20260222 T3977452675 No Limit Hold'em 21K + 3,750.txt|25000.00|153000.00|1|false",
					"HH20260224 T3978140632 No Limit Hold'em 21K + 3,750.txt|25000.00|28687.00|2|false",
					"HH20260224 T3978149984 No Limit Hold'em 21K + 3,750.txt|25000.00|0.00|24|false" })
	void should_resolve_real_hand_history_files(String fileName, String buyIn, String prize, Integer position,
			boolean realMoney) throws IOException {
		String content = Files.readString(Path.of("src/test/resources/GuiRodri2013", fileName));

		for (String rawHand : content.split("(\\r?\\n){2,}")) {
			acceptHand(rawHand);
		}

		assertThat(collector.toResult())
			.isEqualTo(new TournamentResult(ChipsUtils.of(buyIn), ChipsUtils.of(prize), position, realMoney, true));
	}

	@Test
	void should_resolve_finish_without_position() {
		acceptHand("GuiRodri2013 finished the tournament");

		assertThat(collector.toResult()).isEqualTo(new TournamentResult(ZERO, ChipsUtils.of(0.0), null, false, true));
	}

	@Test
	void should_build_tournament_session_with_prize_minus_buy_in() {
		Instant startTime = Instant.ofEpochSecond(1771774381);
		Instant endTime = startTime.plusSeconds(60);
		acceptHand(heroWinsTournament());

		PokerSession session = collector.toSession("My Tournament", 1, startTime, endTime);

		assertThat(session).isEqualTo(TournamentPokerSession.of("My Tournament", 1, startTime, endTime,
				new TournamentResult(ChipsUtils.of(0.50), ChipsUtils.of(5.67), 1, true, true)));
		assertThat(session.getNetProfit()).isEqualTo(ChipsUtils.of(5.17));
	}

	@Test
	void should_ignore_the_hands_chip_results() {
		acceptHand(heroWinsTournament());
		collector.accept(List.of(), handWithNetProfitAt("16822.00", Instant.ofEpochSecond(1771774381)));

		assertThat(collector.toSession("My Tournament", 2, null, null).getNetProfit()).isEqualTo(ChipsUtils.of(5.17));
	}

	private void acceptHand(String rawHand) {
		collector.accept(parser.parse(rawHand), null);
	}

}
