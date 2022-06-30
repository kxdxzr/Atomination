# Atomination

This a game called Atomination.

## Game rule
Game rules and features:  
There is a game board that is n x m grid spaces  
There are k players and each player takes a turn starting from player 1. there is a minimum of 2 players  
and a maximum of 4 players per a game  
Initially a grid space is unoccupied until a player places an atom in that grid space  
A player can place an atom on a grid space that they already own or is unoccupied  
If the grid space is a corner, then the limit is 2  
If the grid space is on a side then the limit is 3  
If the grid space is neither a corner or a side space then the limit is 4 (The pattern is the adjacent grid spaces)  
Once a grid space has reached it's limit ( number of atoms >= limit ), a single atom will expand out to the adjacent grid spaces, capturing them if they are owned by an opponent  
After the first k moves (k being the number of players in the game), players can be removed from the game if they no longer own any grid spaces  
Players can undo moves that they have performed  
The maximum width of the board is 255, the minimum width is 2  
The maximum height of the board is 255, the minimum height is 2  
You may assume the maximum line length is 255  
There is an option to save the game and load it when the program has been reloaded  
The player colour order is RGPB (Red, Green, Purple, Blue)  
The user interacts with a set of commands that are specified later in this document  
