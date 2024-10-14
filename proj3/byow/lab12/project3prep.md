# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:
My implementation generates hexagons from a given central point by calculating neighbors in rings.
The staff's solution draws hexagons by rows and columns.
-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:
In tessellation problem hexagons must be side by side, so randomness is limited to the position of the first hexagon.
On the Project 3 side hexagons are rooms and tesselation is theirs positioning.
-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:
The first method was the one drawing the draw single hexagon.
-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: 
Similarity is in the fact that rooms and hallways have floor and walls.
However, the hallway should have a width of 1 or 2 tiles and rooms have random width and height of rooms should be random.