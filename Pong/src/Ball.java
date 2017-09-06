import java.util.Random;
import static org.lwjgl.opengl.GL11.*;

public class Ball {
    //Variables for the ball.
    private float px, py, dirx, diry, velocity;
    private final float sx, sy, max_velocity;
    private boolean moving; 
    private final float[] dirsx = {.5f, -.4f, .3f, -.2f, -.5f, .4f, -.3f, .2f};
    
    //Default constructor for the ball class, called when "new Ball() is called
    //from anywhere with access.
    Ball(float xp, float yp){
        px = xp; py = yp; 
        moving = false;
        velocity = 1f;
        sx = 0.01f; 
        sy =0.015f;
        Random ran = new Random();
        dirx = dirsx[ran.nextInt(8)];
        diry = 0f;
        max_velocity = 8f;
    }
    
    //Move function to, well, move the ball.
    private void MoveBall(double dt){
        px += dirx * velocity * dt;
        py += diry * velocity * dt;
    }
    
    //Render function for the representation of the ball
    public void Render(){
        glPushMatrix();
            glTranslatef(px, py, 0.0f);
            glBegin(GL_TRIANGLES);
                glColor3f(1.0f, 0.0f, 0.0f);
                glVertex2f(-sx, sy);
                glVertex2f(-sx, -sy);
                glVertex2f(sx, sy);
                glVertex2f(sx, sy);
                glVertex2f(-sx, -sy);
                glVertex2f(sx, -sy);
            glEnd();
        glPopMatrix();
        //System.out.println(px + " " + py);
    }
    
    //Reset function for the ball called when a player scores
    public void Reset(){
        px = py = 0f;
        moving = true;
        diry = 0f;
        Random ran = new Random();
        dirx = dirsx[ran.nextInt(8)];
        velocity = 1.25f;
    }
    
    //Update function for the ball
    public void Update(double dt){
        if(moving){
            MoveBall(dt);
        }
    }
    
    //Get and set velocity
    public float getVelocity(){return velocity;}
    public void setVelocity(float value){
        if(value <= max_velocity)
            velocity = value;
    }
    //Get and set the direction on the x axis
    public float getDirX(){return dirx;}
    public void setDirX(float value){dirx = value;}
    //Get and set the direction on the y axis
    public float getDirY(){return diry;}
    public void setDirY(float value){diry = value;}
    //Get the size on the x axis
    public float getSizeX(){return sx;}
    //Get the size on the y axis
    public float getSizeY(){return sy;}
    //Get and set the boolean variable for whether the ball is moving
    public boolean getMoving(){return moving;}
    public void setMoving(boolean value){moving = value;}
    //Get and set the position of the ball on the x axis
    public float getPosX(){return px;}
    public void setPosX(float value){px = value;}
    //Get and set the position of the ball on the y axis
    public float getPosY(){return py;}
    public void setPosY(float value){py = value;}
}
