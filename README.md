# Really Interesting Strategic Conquest game in Java

The game developed is a multi-module gradle project that adopts Socket programming and multi-threading using Java Callables.

In this project, I have developed a board game inspired by the board game “RISK,” but with very different rules.
1. First of all, each player’s goal is to conquer “the world.” This work is composed of a set of territories—areas on the map which are each controlled by one player. 

2. Game play consists primarily of two types of actions (at least for this Evolution): move and
attack. A move action lets a player relocate units within their own territories. An attack order allows a player to send their units to an adjacent territory controlled by a different player, in an attempt to gain control over that territory. When an attack happens, the attacking units “fight” the defending units. A battle lasts until either the attacker or defender runs out of units participating in the battle, at which point the side with units remaining controls the territory.

3. One major difference between this game and most board games is that all players take their turns at the same time. The players enter 0 or more moves, then commit their moves — they say that they are done entering moves for this turn and do not wish to make changes. Once all players commit their moves, the game resolves the outcome of all moves, then reports that to the players, then the next turn happens in the same fashion.

Whenever a player has no more territories, that player loses. When one player has all territories,
that player wins.
