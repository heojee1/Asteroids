# Asteroids

## Introduction

Included in this assignment, you'll find a working single-player version of the classic "Asteroids" arcade game, whose objective is to shoot as many asteroids as possible without crashing your ship.

### Out-of-the-box Gameplay & Controls

When you first boot up the game, you'll notice a number in the top-left of your screen, as well as a green energy bar. The number is your score, indicating how many asteroids you've destroyed, and the energy bar shows the status of your ship's onboard capacitor banks. Each time you move the ship or fire your weapon, this will draw some energy from your ship, and when your ship runs out of energy, don't expect your thrusters or weapons to work reliably, so keep an eye on that. Over time though, your ship's solar cells will generate energy, but not fast enough to handle many costly operations at once.

> To run this program standalone, you can create a JAR file with the following maven command:
>
> ```
> mvn clean package
> ```
>
> The JAR file should appear in the `/target` directory.

By default, the controls for the game are as follows:

- **Accelerate Forward** - `W`
- **Turn Left** - `A`
- **Turn Right** - `D`
- **Fire Weapon** - `SPACE`

In the top of the game window, you'll see a menu where you have the option to either start a new game, or quit the game.

### Getting Familiar with the Code

Before you begin improving upon this base game, please take some time to browse through the source code. Every class is complete with Javadoc strings for every method and instance variable, but here is a brief description of structure of the project:

- The project as a whole follows the MVC design pattern. Anything pertaining to the game's model is found in the `model` package, view things such as Swing UI components are found in the `view` package, and yes, you guessed it, you'll find controllers in the `control` package.
- The main model containing all of the information about the state of the game is `aoop.asteroids.model.Game`. This model consists of a `Spaceship`, some `Bullet` objects and some `Asteroid` objects.
- When starting the game, an `AsteroidsFrame` is created, and it contains an `AsteroidsPanel`. This panel is responsible for drawing all the objects in the game each time the screen refreshes. However, the panel itself doesn't contain the code that draws each object. Instead, you'll find that in the `view.view_models` package. When the panel wants to draw the spaceship, for example, it will construct a new view model for the game's spaceship, and call that view model's `drawObject()` method.
- The actual game loop and physics updates can be found in `GameUpdater`. This class implements `Runnable`, and essentially runs the game's logic in a separate thread, and once in a while asks the asteroids panel to repaint itself.
  - Because we don't want to do what Skyrim did and lock the game physics to the FPS, this game allows you to change the FPS while keeping the physics update rate (tick) the same. In short, this is done by iterating continuously without sleeping, and updating the physics and the display at different intervals, completely independent of each other. However, it's not that simple, since if the FPS is higher than the tick rate, there is a chance that the display will be refreshed twice between ticks. Since no physics update was done in that time, all objects will be in the same position as before. To avoid this issue, we simply assume that the object keeps moving at the same speed it has before, in the same direction as before. By doing this, the game appears smoother when a higher FPS is set. For a more detailed look at how this works, take a look at `GameObjectViewModel::drawObject()`. If you'd like a more in-depth explanation that's beyond the scope of this course, consider [this article](https://gameprogrammingpatterns.com/game-loop.html).

## Assignment

While some might say this game is already quite impressive, we want you to do better, by making it multiplayer. More specifically, here are some things we're going to look for when grading your project:

- Stable multiplayer functionality across multiple machines using UDP.
- A main menu from which the user can select at least these four options:
  1. Start singleplayer game.
  2. Host a multiplayer game.
  3. Spectate a multiplayer game.
  4. Join a multiplayer game.
- Different players'  spaceships should be able to destroy each other with their weapons.
- Each player should have a nickname, and some sort of visual attribute, such as a differently colored ship.
- Persistent high scores, stored in a database. This can be done in an ObjectDb database, SQLite3, MySQL, etc. Since scoring a multiplayer game is up to your interpretation, and because the scores from a multiplayer game may very likely be meaningless from one game to the next (due to a varying number of players, for example), at the very minimum we require a persistent high scores database for all singleplayer games.
- Players (or spectators) connected to a multiplayer game should be able to quit (and not just by calling `System.exit(0);`, don't be lazy). Quitting should bring them back to the main menu.
- The server should be able to stop hosting the game, causing all connected players to disconnect, sending them back to the main menu.
- When in a multiplayer game, a player (or spectator) should see the score of all connected players. Whether or not you show players each others' energy levels is up to you.

> In previous years, the guidelines were slightly more strict. Since all this does is hinder creativity, we will not tell you how to compute the score of a multiplayer game, or whether or not ships can collide with each other, or a server of other minor details. These things are up to you to decide.

### Report

Because of the size of this project, we are requiring that you submit a report along with your submission of the assignment code. This report must discuss the following things:

1. **Problem Description and Analysis:** What do you have to do to complete this assignment successfully? What is your plan for completing the assignment? What are you going to work on first? How will you split up the work? How are you planning on structuring things? Essentially, divulge your plans for how you intend to implement all of the required functionality, as well as any extra functionality you've thought of by this point.
2. **Design Description:** Describe your program's structure (classes and packages) in detail, addressing all but the most trivial features, and provide ample reasoning for why you chose a certain structure, or why you implemented something a certain way. What design patterns did you use? Describe how and where they've been applied. And finally, how does your game handle networking? Give a description of the protocol or messages that the clients use to communicate with servers.
3. **Evaluation:** Discuss the stability of your implementation. What works well? Are there any bugs? Are there still features that have not been implemented? Also, if you had the time, what improvements would you make to your implementation? Are there things which you would have done completely differently?
4. **Teamwork:** What did each team member contribute to the assignment? Not just in terms of code, but also more abstractly, such as, "Tommy upgraded the game model to support multiple ships.", or "William designed the protocol that clients use for communicating with the server."

> Aside from relevant code snippets, there's no need to include the entirety of your code in the report; we can just look on Github.com.

It does not matter what software/hardware you use to generate your report; we're not going to crucify you for not using Latex. Just make sure that what you write is clearly legible, organized, and with at least decent grammar. If you hand in something handwritten that's completely illegible, you will not get a grade for the report. Furthermore, if both students' names and student number, as well as team number, are not clearly visible at the beginning of the report, it will not be graded.

### Extras

With such an open-ended project, there is a lot of room for adding additional features to the program, and these can improve your grade. Here are some ideas to give you a bit of inspiration:

- Different game modes (Multiplayer Co-op, 1v1 duel, and more).
- Improved graphics or audio.
- Different / More weapons on a ship.
- Other game objects besides asteroids, such as rogue AI ships.
- In-game chat or some form of communication.
- A ranking system for multiplayer games. This is rather complex, but if you do something like this, we'll be very impressed.
- Integration with external applications, such as a website that displays high scores.