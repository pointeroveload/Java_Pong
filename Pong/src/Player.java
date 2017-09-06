import static org.lwjgl.opengl.GL11.*;

public class Player {
    //Variables used by the player class. 
    private int score; 
    private boolean ai;
    private final boolean debug;
    private float px, py, sx, sy;
    
    //Default constructor for the Player class, used whenever "new Player()" is 
    //called from anywhere that has access.
    Player(float xp, float yp){
        px = xp; py = yp;
        sx = .01f;
        sy = .1f;
        score = 0;
        debug = true;
    }
    
    //Check collision with a player, takes a reference to the ball used in the
    //game for ease of accessing the current ball location etc.
    public void CheckCollision(Ball b){

        if( b.getDirX() < 0f ){
            if( (b.getPosX() - b.getSizeX() ) < px + sx){
                //if we're in the upper 
                if( b.getPosY() > py+sy/2 && b.getPosY() < py+sy){
                    b.setDirX(-b.getDirX());
                    b.setDirY(b.getDirY() + .25f);
                }
                //if we are in the lower
                else if( b.getPosY() < py-sy/2 && b.getPosY() > py-sy){
                    b.setDirX(-b.getDirX());
                    b.setDirY(b.getDirY() - .25f); 
                }
                else if( b.getPosY() > py-sy && b.getPosY() < py+sy)
                    b.setDirX(-b.getDirX());
                b.setVelocity(b.getVelocity() * 1.10f);
            }      
        }else{
             if( (b.getPosX() + b.getSizeX() ) > px - sx){
                //if we're in the upper 
                if( b.getPosY() > py+sy/2 && b.getPosY() < py+sy){
                    b.setDirX(-b.getDirX());
                    b.setDirY(b.getDirY() + .25f);
                }
                //if we are in the lower
                else if( b.getPosY() < py-sy/2 && b.getPosY() > py-sy){
                    b.setDirX(-b.getDirX());
                    b.setDirY(b.getDirY() - .25f); 
                }
                else if( b.getPosY() > py-sy && b.getPosY() < py+sy)
                    b.setDirX(-b.getDirX());
                b.setVelocity(b.getVelocity() * 1.10f);
            }
        }
    }
    
    //Function used when needing to move the player, takes a direction and a
    //double representating delta time
    public void MovePlayer(int dir, double dt){
        //move the player on the y axis only    
        float moveAmount = 1f;
        switch(dir){
            case 0: 
                if( py < ( 0.98f - sy ) )
                    py += (moveAmount * dt);
                break;
            case 1: //move down
                if( py > ( -0.98f + sy) )
                    py -= (moveAmount * dt);
                break;
            default:
                break;
        }
    }
    
    //Render function of the representation of the player
    public void Render(){
        //player rect
        glPushMatrix();
            glTranslatef(px, py, 0.0f);
            glBegin(GL_TRIANGLES);
                glColor3f(1.0f, 1.0f, 1.0f);
                glVertex2f(-sx, sy);
                glVertex2f(-sx, -sy);
                glVertex2f(sx, sy);
                glVertex2f(sx, sy);
                glVertex2f(-sx, -sy);
                glVertex2f(sx, -sy);
            glEnd();
        glPopMatrix();
        
        if(debug){
            glPushMatrix();
                glBegin(GL_LINES);
                    glColor3f(0f, 1f, 0f);
                    glVertex2f(px-sx, py+sy/2);
                    glVertex2f(px+sx, py+sy/2);
                    glColor3f(0f, 1f, 0f);
                    glVertex2f(px-sx, py-sy/2);
                    glVertex2f(px+sx, py-sy/2);
                glEnd();
            glPopMatrix();
        }
        
    }
    
    //Reset the player to original position
    public void Reset(){
        py = 0f;
    }
    
    //Player update function
    public void Update(){

    }
    
    /// Getters and setters
    //(Unused) Get and set whether the player is AI controlled
    public boolean getAI(){return ai;}
    public void setAI(boolean value){ai = value;}
    //Get and set the players' score
    public int getScore(){return score;}
    public void setScore(int value){score = value;}
    //Get and set the position of the player on the x axis
    public float getPosX(){return px;}
    public void setPosX(float value){px = value;}
    //Get and set the position of the player on the y axis
    public float getPosY(){return py;}
    public void setPosY(float value){py = value;}
    //Get and set the size of the player on the x axis
    public float getSizeX(){return sx;}
    public void setSizeX(float value){sx = value;}
    //Get and set the size of the player on the y axis
    public float getSizeY(){return sy;}
    public void setSizeY(float value){sy = value;}    
}
