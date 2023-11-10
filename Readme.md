# Asteroids

## Introduction

This is Java implementation of classic "Asteroid" game, which can be played in a single- or multi-player mode.

### Gameplay & Controls

When you first boot up the game, you'll notice a number in the top-left of your screen, as well as a green energy bar. The number is your score, indicating how many asteroids you've destroyed, and the energy bar shows the status of your ship's onboard capacitor banks. Each time you move the ship or fire your weapon, this will draw some energy from your ship, and when your ship runs out of energy, don't expect your thrusters or weapons to work reliably, so keep an eye on that. Over time though, your ship's solar cells will generate energy, but not fast enough to handle many costly operations at once.

- **Accelerate Forward** - `W`
- **Turn Left** - `A`
- **Turn Right** - `D`
- **Fire Weapon** - `SPACE`


### Multiplayer Mode
You can create a "waiting room" by opting to host a multiplayer game.
Other players can join via IP address shown in the room.
The game starts when the host clicks on the "Start" button.

### Other functionalitieis
The results are stored in a MySQL DB and can be viewed as a leaderboard.
