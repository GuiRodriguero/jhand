package com.gui.jhand.hand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class HandTemplateLoader {

	static String validAsString() {
		String content;
		Path filePath = getRandomGame();
		try {
			content = Files.readString(filePath);
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading file: " + filePath, e);
		}

		String[] handsAsString = content.split("(\\R){3,}");

		List<String> hands = Arrays.stream(handsAsString)
				.map(String::trim)
				.filter(block -> !block.isEmpty())
				.toList();

		return hands.get((int) (Math.random() * hands.size()));
	}

	private static Path getRandomGame() {
		String userName = "GuiRodri2013";
		Path userPath = Paths.get("src", "test", "resources", userName).toAbsolutePath();

		List<Path> files;
		try (Stream<Path> paths = Files.list(userPath)) {
			files = paths.filter(Files::isRegularFile).toList();
		} catch (IOException e) {
			throw new RuntimeException("Error listing files in directory: " + userPath, e);
		}

		if (files.isEmpty()) {
			throw new RuntimeException("No files found in directory: " + userPath);
		}

		return files.get((int) (Math.random() * files.size()));
	}

	static String heroAtBtn() {
		return """
				PokerStars Hand #259859224424: Tournament #3978149677,  Hold'em No Limit - Level II (30/60) - 2026/02/24 20:13:13 BRT [2026/02/24 18:13:13 ET]
				Table '3978149677 1' 4-max Seat #1 is the button
				Seat 1: GuiRodri2013 (2900 in chips, 9000 bounty)
				Seat 2: villain1 (1100 in chips, 15760 bounty)
				GuiRodri2013: posts small blind 30
				villain1: posts big blind 60
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [2d 3h]
				GuiRodri2013: folds
				Uncalled bet (30) returned to villain1
				villain1 collected 60 from pot
				villain1: doesn't show hand
				*** SUMMARY ***
				Total pot 60 | Rake 0
				Seat 1: GuiRodri2013 (button) (small blind) folded before Flop
				Seat 2: villain1 (big blind) collected (60)
				""";
	}

	static String heroAtSB() {
		return """
				PokerStars Hand #259859027059: Tournament #3978140632, 10625+10625+3750 Hold'em No Limit - Level VIII (75/150) - 2026/02/24 19:56:51 BRT [2026/02/24 17:56:51 ET]
				Table '3978140632 1' 9-max Seat #1 is the button
				Seat 1: villain1 (881 in chips)
				Seat 2: GuiRodri2013 (1288 in chips)
				Seat 4: villain3 (352 in chips)
				Seat 6: villain5 (464 in chips)
				Seat 9: villain8 (1515 in chips)
				villain1: posts the ante 15
				GuiRodri2013: posts the ante 15
				villain3: posts the ante 15
				villain5: posts the ante 15
				villain8: posts the ante 15
				GuiRodri2013: posts small blind 75
				villain3: posts big blind 150
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [Kc Kd]
				villain5: folds
				villain8: folds
				villain1: folds
				GuiRodri2013: raises 300 to 450
				villain3: calls 187 and is all-in
				Uncalled bet (113) returned to GuiRodri2013
				*** FLOP *** [Ac 7d Th]
				*** TURN *** [Ac 7d Th] [9c]
				*** RIVER *** [Ac 7d Th 9c] [Ts]
				*** SHOW DOWN ***
				GuiRodri2013: shows [Kc Kd] (two pair, Kings and Tens)
				villain3: shows [Kh 7h] (two pair, Tens and Sevens)
				GuiRodri2013 collected 749 from pot
				GuiRodri2013 wins the 10625 bounty for eliminating villain3
				villain3 finished the tournament in 5th place
				*** SUMMARY ***
				Total pot 749 | Rake 0
				Board [Ac 7d Th 9c Ts]
				Seat 1: villain1 (button) folded before Flop (didn't bet)
				Seat 2: GuiRodri2013 (small blind) showed [Kc Kd] and won (749) with two pair, Kings and Tens
				Seat 4: villain3 (big blind) showed [Kh 7h] and lost with two pair, Tens and Sevens
				Seat 6: villain5 folded before Flop (didn't bet)
				Seat 9: villain8 folded before Flop (didn't bet)
				""";
	}

	static String heroAtBB() {
		return """
				PokerStars Hand #259859225434: Tournament #3978149677,  Hold'em No Limit - Level II (30/60) - 2026/02/24 20:13:18 BRT [2026/02/24 18:13:18 ET]
				Table '3978149677 1' 4-max Seat #2 is the button
				Seat 1: GuiRodri2013 (2870 in chips, 9000 bounty)
				Seat 2: villain1 (1130 in chips, 15760 bounty)
				villain1: posts small blind 30
				GuiRodri2013: posts big blind 60
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [9c 2c]
				villain1: calls 30
				GuiRodri2013: checks
				*** FLOP *** [Qc 6c 6h]
				GuiRodri2013: checks
				villain1: checks
				*** TURN *** [Qc 6c 6h] [Th]
				GuiRodri2013: checks
				villain1: checks
				*** RIVER *** [Qc 6c 6h Th] [7c]
				GuiRodri2013: bets 120
				villain1: folds
				Uncalled bet (120) returned to GuiRodri2013
				GuiRodri2013 collected 120 from pot
				GuiRodri2013: doesn't show hand
				*** SUMMARY ***
				Total pot 120 | Rake 0
				Board [Qc 6c 6h Th 7c]
				Seat 1: GuiRodri2013 (big blind) collected (120)
				Seat 2: villain1 (button) (small blind) folded on the River
				""";
	}

	static String heroAtUTG() {
		return """
				PokerStars Hand #259859019151: Tournament #3978140632, 10625+10625+3750 Hold'em No Limit - Level VIII (75/150) - 2026/02/24 19:56:01 BRT [2026/02/24 17:56:01 ET]
				Table '3978140632 1' 9-max Seat #6 is the button
				Seat 1: villain1 (686 in chips)
				Seat 2: GuiRodri2013 (1618 in chips)
				Seat 4: villain3 (382 in chips)
				Seat 6: villain5 (494 in chips)
				Seat 9: villain8 (1320 in chips)
				villain1: posts the ante 15
				GuiRodri2013: posts the ante 15
				villain3: posts the ante 15
				villain5: posts the ante 15
				villain8: posts the ante 15
				villain8: posts small blind 75
				villain1: posts big blind 150
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [8h 7d]
				GuiRodri2013: folds
				villain3: folds
				villain5: folds
				villain8: raises 150 to 300
				villain1: folds
				Uncalled bet (150) returned to villain8
				villain8 collected 375 from pot
				villain8: doesn't show hand
				*** SUMMARY ***
				Total pot 375 | Rake 0
				Seat 1: villain1 (big blind) folded before Flop
				Seat 2: GuiRodri2013 folded before Flop (didn't bet)
				Seat 4: villain3 folded before Flop (didn't bet)
				Seat 6: villain5 (button) folded before Flop (didn't bet)
				Seat 9: villain8 (small blind) collected (375)
				""";
	}

	static String heroAtMP() {
		return """
				PokerStars Hand #259858883233: Tournament #3978140632, 10625+10625+3750 Hold'em No Limit - Level II (15/30) - 2026/02/24 19:45:41 BRT [2026/02/24 17:45:41 ET]
				Table '3978140632 1' 9-max Seat #4 is the button
				Seat 1: villain1 (413 in chips)
				Seat 2: GuiRodri2013 (831 in chips)
				Seat 3: villain2 (463 in chips)
				Seat 4: villain3 (338 in chips)
				Seat 5: villain4 (525 in chips)
				Seat 6: villain5 (203 in chips)
				Seat 7: villain6 (991 in chips)
				Seat 8: villain7 (323 in chips)
				Seat 9: villain8 (413 in chips)
				villain1: posts the ante 3
				GuiRodri2013: posts the ante 3
				villain2: posts the ante 3
				villain3: posts the ante 3
				villain4: posts the ante 3
				villain5: posts the ante 3
				villain6: posts the ante 3
				villain7: posts the ante 3
				villain8: posts the ante 3
				villain4: posts small blind 15
				villain5: posts big blind 30
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [7h Ad]
				villain6: folds
				villain7: folds
				villain8: folds
				villain1: folds
				GuiRodri2013: raises 60 to 90
				villain2: folds
				villain3: folds
				villain4: calls 75
				villain5: calls 60
				*** FLOP *** [Qs 9d Qh]
				villain4: checks
				villain5: checks
				GuiRodri2013: checks
				*** TURN *** [Qs 9d Qh] [3h]
				villain4: checks
				villain5: checks
				GuiRodri2013: checks
				*** RIVER *** [Qs 9d Qh 3h] [6s]
				villain4: checks
				villain5: checks
				GuiRodri2013: checks
				*** SHOW DOWN ***
				villain4: shows [4s 8s] (a pair of Queens)
				villain5: shows [Kd As] (a pair of Queens - Ace kicker)
				GuiRodri2013: mucks hand
				villain5 collected 297 from pot
				*** SUMMARY ***
				Total pot 297 | Rake 0
				Board [Qs 9d Qh 3h 6s]
				Seat 1: villain1 folded before Flop (didn't bet)
				Seat 2: GuiRodri2013 mucked [7h Ad]
				Seat 3: villain2 folded before Flop (didn't bet)
				Seat 4: villain3 (button) folded before Flop (didn't bet)
				Seat 5: villain4 (small blind) showed [4s 8s] and lost with a pair of Queens
				Seat 6: villain5 (big blind) showed [Kd As] and won (297) with a pair of Queens
				Seat 7: villain6 folded before Flop (didn't bet)
				Seat 8: villain7 folded before Flop (didn't bet)
				Seat 9: villain8 folded before Flop (didn't bet)
				""";
	}

	static String heroAtMPWith8Players() {
		return """
				PokerStars Hand #259858920485: Tournament #3978140632, 10625+10625+3750 Hold'em No Limit - Level IV (30/60) - 2026/02/24 19:48:24 BRT [2026/02/24 17:48:24 ET]
				Table '3978140632 1' 9-max Seat #6 is the button
				Seat 1: villain1 (366 in chips)
				Seat 2: GuiRodri2013 (734 in chips)
				Seat 3: villain2 (336 in chips)
				Seat 4: villain3 (1349 in chips)
				Seat 6: villain5 (283 in chips)
				Seat 7: villain6 (750 in chips)
				Seat 8: villain7 (276 in chips)
				Seat 9: villain8 (406 in chips)
				villain1: posts the ante 6
				GuiRodri2013: posts the ante 6
				villain2: posts the ante 6
				villain3: posts the ante 6
				villain5: posts the ante 6
				villain6: posts the ante 6
				villain7: posts the ante 6
				villain8: posts the ante 6
				villain6: posts small blind 30
				villain7: posts big blind 60
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [9s 2h]
				villain8: raises 120 to 180
				villain1: folds
				GuiRodri2013: folds
				villain2: folds
				villain3: folds
				villain5: folds
				villain6: raises 564 to 744 and is all-in
				villain7: folds
				villain8: calls 220 and is all-in
				Uncalled bet (344) returned to villain6
				*** FLOP *** [5c 9d Th]
				*** TURN *** [5c 9d Th] [4h]
				*** RIVER *** [5c 9d Th 4h] [4s]
				*** SHOW DOWN ***
				villain6: shows [Ks 8d] (a pair of Fours)
				villain8: shows [Jh Ad] (a pair of Fours - Ace kicker)
				villain8 collected 908 from pot
				*** SUMMARY ***
				Total pot 908 | Rake 0
				Board [5c 9d Th 4h 4s]
				Seat 1: villain1 folded before Flop (didn't bet)
				Seat 2: GuiRodri2013 folded before Flop (didn't bet)
				Seat 3: villain2 folded before Flop (didn't bet)
				Seat 4: villain3 folded before Flop (didn't bet)
				Seat 6: villain5 (button) folded before Flop (didn't bet)
				Seat 7: villain6 (small blind) showed [Ks 8d] and lost with a pair of Fours
				Seat 8: villain7 (big blind) folded before Flop
				Seat 9: villain8 showed [Jh Ad] and won (908) with a pair of Fours
				""";
	}

	static String heroAtCO() {
		return """
				PokerStars Hand #259858866241: Tournament #3978140632, 10625+10625+3750 Hold'em No Limit - Level II (15/30) - 2026/02/24 19:44:27 BRT [2026/02/24 17:44:27 ET]
				Table '3978140632 1' 9-max Seat #3 is the button
				Seat 1: villain1 (416 in chips)
				Seat 2: GuiRodri2013 (834 in chips)
				Seat 3: villain2 (466 in chips)
				Seat 4: villain3 (356 in chips)
				Seat 5: villain4 (336 in chips)
				Seat 6: villain5 (296 in chips)
				Seat 7: villain6 (1024 in chips)
				Seat 8: villain7 (356 in chips)
				Seat 9: villain8 (416 in chips)
				villain1: posts the ante 3
				GuiRodri2013: posts the ante 3
				villain2: posts the ante 3
				villain3: posts the ante 3
				villain4: posts the ante 3
				villain5: posts the ante 3
				villain6: posts the ante 3
				villain7: posts the ante 3
				villain8: posts the ante 3
				villain3: posts small blind 15
				villain4: posts big blind 30
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [6c Ad]
				villain5: calls 30
				villain6: calls 30
				villain7: calls 30
				villain8: folds
				villain1: folds
				GuiRodri2013: folds
				villain2: folds
				villain3: folds
				villain4: checks
				*** FLOP *** [8c 4d 7d]
				villain4: bets 30
				villain5: calls 30
				villain6: folds
				villain7: folds
				*** TURN *** [8c 4d 7d] [9c]
				villain4: bets 30
				villain5: calls 30
				*** RIVER *** [8c 4d 7d 9c] [6d]
				villain4: bets 141
				villain5: folds
				Uncalled bet (141) returned to villain4
				villain4 collected 282 from pot
				*** SUMMARY ***
				Total pot 282 | Rake 0
				Board [8c 4d 7d 9c 6d]
				Seat 1: villain1 folded before Flop (didn't bet)
				Seat 2: GuiRodri2013 folded before Flop (didn't bet)
				Seat 3: villain2 (button) folded before Flop (didn't bet)
				Seat 4: villain3 (small blind) folded before Flop
				Seat 5: villain4 (big blind) collected (282)
				Seat 6: villain5 folded on the River
				Seat 7: villain6 folded on the Flop
				Seat 8: villain7 folded on the Flop
				Seat 9: villain8 folded before Flop (didn't bet)
				""";
	}
	
	static String validWonWithShowdown() {
		return """
				PokerStars Hand #259827261990: Tournament #3977452675, 21250+3750 Hold'em No Limit - Level X (400/800) - 2026/02/22 12:33:01 BRT [2026/02/22 10:33:01 ET]
				Table '3977452675 1' 9-max Seat #7 is the button
				Seat 1: GuiRodri2013 (2184 in chips)
				Seat 3: villain2 (8860 in chips)
				Seat 7: villain6 (10675 in chips)
				Seat 8: villain10 (4311 in chips)
				Seat 9: villain8 (970 in chips)
				GuiRodri2013: posts the ante 50
				villain2: posts the ante 50
				villain6: posts the ante 50
				villain10: posts the ante 50
				villain8: posts the ante 50
				villain10: posts small blind 400
				villain8: posts big blind 800
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [Qd Jh]
				GuiRodri2013: raises 1334 to 2134 and is all-in
				villain2: folds
				villain6: folds
				villain10: folds
				villain8: calls 120 and is all-in
				Uncalled bet (1214) returned to GuiRodri2013
				*** FLOP *** [Jc 6c 2c]
				*** TURN *** [Jc 6c 2c] [8s]
				*** RIVER *** [Jc 6c 2c 8s] [As]
				*** SHOW DOWN ***
				villain8: shows [6s 3h] (a pair of Sixes)
				GuiRodri2013: shows [Qd Jh] (a pair of Jacks)
				GuiRodri2013 collected 2490 from pot
				villain8 finished the tournament in 5th place
				*** SUMMARY ***
				Total pot 2490 | Rake 0
				Board [Jc 6c 2c 8s As]
				Seat 1: GuiRodri2013 showed [Qd Jh] and won (2490) with a pair of Jacks
				Seat 3: villain2 folded before Flop (didn't bet)
				Seat 7: villain6 (button) folded before Flop (didn't bet)
				Seat 8: villain10 (small blind) folded before Flop
				Seat 9: villain8 (big blind) showed [6s 3h] and lost with a pair of Sixes
				""";
	}

	static String validWonWithoutShowdown() {
		return """
				PokerStars Hand #259827317520: Tournament #3977452675, 21250+3750 Hold'em No Limit - Level XI (600/1200) - 2026/02/22 12:38:02 BRT [2026/02/22 10:38:02 ET]
				Table '3977452675 1' 9-max Seat #7 is the button
				Seat 1: GuiRodri2013 (3508 in chips)
				Seat 7: villain6 (23492 in chips)
				GuiRodri2013: posts the ante 75
				villain6: posts the ante 75
				villain6: posts small blind 600
				GuiRodri2013: posts big blind 1200
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [3d 8c]
				villain6: calls 600
				GuiRodri2013: checks
				*** FLOP *** [8d Qd 8h]
				GuiRodri2013: bets 2233 and is all-in
				villain6: folds
				Uncalled bet (2233) returned to GuiRodri2013
				GuiRodri2013 collected 2550 from pot
				GuiRodri2013: doesn't show hand
				*** SUMMARY ***
				Total pot 2550 | Rake 0
				Board [8d Qd 8h]
				Seat 1: GuiRodri2013 (big blind) collected (2550)
				Seat 7: villain6 (button) (small blind) folded on the Flop
				""";
	}
	
	static String validWithSidePot() {
		return """
				PokerStars Hand #259859276702: Tournament #3978149984, 21250+3750 Hold'em No Limit - Level I (10/20) - 2026/02/24 20:17:19 BRT [2026/02/24 18:17:19 ET]
				Table '3978149984 4' 9-max Seat #4 is the button
				Seat 1: villain1 (1340 in chips)
				Seat 2: villain2 (1484 in chips)
				Seat 4: villain3 (1518 in chips)
				Seat 5: villain4 (1477 in chips)
				Seat 6: villain5 (1474 in chips)
				Seat 7: GuiRodri2013 (1497 in chips)
				Seat 8: villain6 (1494 in chips)
				Seat 9: villain7 (1539 in chips)
				villain1: posts the ante 3
				villain2: posts the ante 3
				villain3: posts the ante 3
				villain4: posts the ante 3
				villain5: posts the ante 3
				GuiRodri2013: posts the ante 3
				villain6: posts the ante 3
				villain7: posts the ante 3
				villain4: posts small blind 10
				villain5: posts big blind 20
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [Ad Ac]
				GuiRodri2013: raises 40 to 60
				villain6: raises 180 to 240
				villain7: folds
				villain1: folds
				villain2: folds
				villain3: folds
				villain4: folds
				villain5: raises 320 to 560
				GuiRodri2013: raises 934 to 1494 and is all-in
				villain6: calls 1251 and is all-in
				villain5: calls 911 and is all-in
				Uncalled bet (3) returned to GuiRodri2013
				*** FLOP *** [Td 7d 9d]
				*** TURN *** [Td 7d 9d] [2c]
				*** RIVER *** [Td 7d 9d 2c] [3h]
				*** SHOW DOWN ***
				GuiRodri2013: shows [Ad Ac] (a pair of Aces)
				villain6: shows [Qd Kh] (high card King)
				GuiRodri2013 collected 40 from side pot
				villain5: shows [As Ah] (a pair of Aces)
				villain5 collected 2224 from main pot
				GuiRodri2013 collected 2223 from main pot
				villain6 finished the tournament
				*** SUMMARY ***
				Total pot 4487 Main pot 4447. Side pot 40. | Rake 0
				Board [Td 7d 9d 2c 3h]
				Seat 1: villain1 folded before Flop (didn't bet)
				Seat 2: villain2 folded before Flop (didn't bet)
				Seat 4: villain3 (button) folded before Flop (didn't bet)
				Seat 5: villain4 (small blind) folded before Flop
				Seat 6: villain5 (big blind) showed [As Ah] and won (2224) with a pair of Aces
				Seat 7: GuiRodri2013 showed [Ad Ac] and won (2263) with a pair of Aces
				Seat 8: villain6 showed [Qd Kh] and lost with high card King
				Seat 9: villain7 folded before Flop (didn't bet)
				""";
	}
	
	static String validWithSidePotAndMultipleWinners() {
		return """
				PokerStars Hand #259859212603: Tournament #3978149677,  Hold'em No Limit - Level I (25/50) - 2026/02/24 20:12:19 BRT [2026/02/24 18:12:19 ET]
				Table '3978149677 1' 4-max Seat #4 is the button
				Seat 1: GuiRodri2013 (950 in chips, 9000 bounty)
				Seat 2: villain1 (2000 in chips, 9000 bounty)
				Seat 4: villain3 (1050 in chips, 13500 bounty)
				GuiRodri2013: posts small blind 25
				villain1: posts big blind 50
				*** HOLE CARDS ***
				Dealt to GuiRodri2013 [Jc Qc]
				villain3: raises 1000 to 1050 and is all-in
				GuiRodri2013: calls 925 and is all-in
				villain1: calls 1000
				*** FLOP *** [3d 2c As]
				*** TURN *** [3d 2c As] [Kh]
				*** RIVER *** [3d 2c As Kh] [Th]
				*** SHOW DOWN ***
				villain1: shows [6c 3c] (a pair of Threes)
				villain3: shows [9s 5h] (high card Ace)
				villain1 collected 200 from side pot
				GuiRodri2013: shows [Jc Qc] (a straight, Ten to Ace)
				GuiRodri2013 collected 2850 from main pot
				villain1 wins 6740 for eliminating villain3 and their own bounty increases by 6760 to 15760
				villain3 finished the tournament in 3rd place
				*** SUMMARY ***
				Total pot 3050 Main pot 2850. Side pot 200. | Rake 0
				Board [3d 2c As Kh Th]
				Seat 1: GuiRodri2013 (small blind) showed [Jc Qc] and won (2850) with a straight, Ten to Ace
				Seat 2: villain1 (big blind) showed [6c 3c] and won (200) with a pair of Threes
				Seat 4: villain3 (button) showed [9s 5h] and lost with high card Ace
				""";
	}

}
