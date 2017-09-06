// run in cmd: java -cp Pong.jar Pong

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Pong{
    //global variable for the application.
    private long window;
    private int wWidth, wHeight;
    private double deltaTime, lastTime;
    //Object instances (scary)
    private Player p1, p2;
    private Ball ball;
    
    
    //Calculate a rough approximation of detla time - (why is this useful?)
    private void CalculateDeltaTime(){
        double currentTime = System.currentTimeMillis();
        deltaTime = (currentTime - lastTime) / 1000;
        lastTime = System.currentTimeMillis();
    }
    
    //Check if there has been a collision between the ball and the boundaries, 
    //or between the ball and the player paddles and then react accordingly
    private void CheckCollision(){
        boolean collision = false;
        float border = .98f;
        //First check if there has been a collision with the bounds. If the side
        //boundaries have been crossed then the opposite player has scored and 
        //the ball is reset, if it is the top or bottom then the ball is reflected.
        {
            if( ball.getPosX() > ( border - ball.getSizeX() ) )
            {
                System.out.println("\tP1 scores!!!");
                p1.setScore(p1.getScore() + 1);
                System.out.println("\t"+p1.getScore()+" | "+p2.getScore());
                ball.Reset();
                p1.Reset();
                p2.Reset();                
            }
            else if ( ball.getPosX() < ( -border + ball.getSizeX() ) )
            {
                System.out.println("\tP2 Scores!");
                p2.setScore(p2.getScore() + 1);
                System.out.println("\t"+p1.getScore()+" | "+p2.getScore());
                ball.Reset();
                p1.Reset();
                p2.Reset();
            }
            else if ( ball.getPosY() > ( border - ball.getSizeY() ) )
            {
                collision = true;
                ball.setDirY(-ball.getDirY());
            }
            else if ( ball.getPosY() < ( -border + ball.getSizeY() ) )
            { 
                collision = true;
                ball.setDirY(-ball.getDirY());
            }
        }
        
        //Check if there is a collision with either p1 or p2 paddles
        {
            if( ball.getDirX() > 0 )
                p2.CheckCollision(ball);
            else
                p1.CheckCollision(ball);
        }
        
        
       if(collision){
           //ball.setVelocity(ball.getVelocity() * 1.5f);
           
       }
    }
    
    //initialise method, used to instantiate variables and set up various instances
    //required for running the application
    private void Init(){
        GLFWErrorCallback.createPrint(System.err).set();
        
        if(!glfwInit()){
            throw new IllegalStateException("Unable to init GLFW!");
        }      
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        window =  glfwCreateWindow(800, 600, "Pong in not quite 3D", NULL, NULL);
        if( window == NULL ){
            glfwTerminate();
            throw new RuntimeException("Could not create a GLFW window!");
        }
        
        //method for handling keyboard input for the application
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
       });
        
        try( MemoryStack stack = stackPush() ){
            IntBuffer pwidth    = stack.mallocInt(1);
            IntBuffer pheight   = stack.mallocInt(1);
            
            glfwGetWindowSize(window, pwidth, pheight);
            
            wWidth = pwidth.get(0); wHeight = pheight.get(0);
            
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            
            glfwSetWindowPos(window, 
                            (vidmode.width() - pwidth.get(0)) / 2, 
                            (vidmode.height() - pheight.get(0)) / 2);
                    
            glfwMakeContextCurrent(window);
            glfwSwapInterval(1);
            glfwShowWindow(window);
        }
        
        //Instantiate the objects used in the application
        p1 = new Player(-.95f, 0f);
        p2 = new Player(.95f, 0f);
        ball = new Ball(0f, 0f);
    }
    
    //Handle any keyboard input for the application, to control each of the players
    private void KeyboardInput(){
        //Player ONE movement input and control
        if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
            p1.MovePlayer(0, deltaTime);
        if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
            p1.MovePlayer(1, deltaTime);

        //Player TWO movement input and control
        if(glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS)
            p2.MovePlayer(0, deltaTime);
        if(glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS)
            p2.MovePlayer(1, deltaTime); 

        if(glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS){
            ball.setMoving(true);
        }
            
    }
    
    //the main loop, aka the game loop. In this method the repetitive functions are 
    //called for use in the application, e.g. render, update window etc
    private void Loop(){
       GL.createCapabilities();
       glClearColor(.5f, .5f, .5f, 0f); // grey
       
       while( !glfwWindowShouldClose(window) ){
           glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
           glfwPollEvents();
           
           Render();
           
           Update();
           
           glfwSwapBuffers(window);
           
           //If the game should end!
           if(p1.getScore() > 4){
               //Player one wins
               System.out.println("Player 1 wins the game with a score of "+p1.getScore()+ " to player 2s "+p2.getScore());
               ball.Reset(); ball.setMoving(false);
               glfwWindowShouldClose(window);break;
           } else if (p2.getScore() > 4){
               //player two wins
               System.out.println("Player 2 wins the game with a score of "+p1.getScore()+ " to player 1s "+p2.getScore());
               ball.Reset(); ball.setMoving(false);
               glfwWindowShouldClose(window);break;
           }
       }
   }
    
    //Print information to the console about the game including instructions
    //and controls etc
    private void Printro(){
        System.out.println(  "Welcome to Pong!\n"
                           + "The game is for two players:\n"
                           + "\tPlayer one controls are 'W' and 'S'\n"
                           + "\tPlayer two controls are 'Up arrow' and 'Down arrow'\n"
                           + "First to five points wins, press spacebar to begin"  
                          );
    }
    
    //Call rendering functions in this method, for each instance that requires
    //rendering. 
    private void Render(){
           p1.Render();
           p2.Render();
           ball.Render();
           ScreenBounds();
    }
    
    //Setup and draw the boundary around the play area
    private void ScreenBounds(){
        float b = .98f;
        glPushMatrix();
            glBegin(GL_LINE_LOOP);
            glLineWidth(1f);
            glColor3f(0f, 0f, 0f);
                glVertex2f(-b, -b);
                glVertex2f(b, -b);
                glVertex2f(b, b);
                glVertex2f(-b, b);
            glEnd();
        glPopMatrix();
    }
    
    //Update function
    private void Update(){
        //Process keyboard input per frame
        KeyboardInput();
        CalculateDeltaTime();
        ball.Update(deltaTime);
        CheckCollision();
    }
    
    //method called by the main method to begin the running of the application, 
    //calls the init and loop methods and will destroy the various instances/objects
    //when the loop is exited.
    public void Run(){

       Printro();
       Init();
       Loop();
       
       glfwFreeCallbacks(window);
       glfwDestroyWindow(window);
       glfwTerminate();
       glfwSetErrorCallback(null).free();
    }
  
    
    
    /// MAIN CLASS METHOD BELOW (DOESN'T REQUIRE EDITING)
    
    //Main function.
    public static void main(String[] args) {
        new Pong().Run();
    }
}
