package com.matthewvu.winghero;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class WingHero extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backGround,bird;
	Texture enemy1, enemy2, enemy3;
	float birdWidth,birdHeight,skullWidth, skullHeight, birdX,birdY,backGroundWidth,backGroundHeight;
	float velocity = 0.0f;
	float gravity = 0.6f;
	int state = 0;
	float enemyX;
	int numEnemies = 3;
	float enemiesX[] = new float[numEnemies];
	float enemiesY[][] = new float[3][numEnemies];

	Circle c_bird, c_enemy1[], c_enemy2[], c_enemy3[];
	int score =0;
	Boolean flag = true;
	Boolean flag1 = true;
	BitmapFont font,font1,font2;
	Sound sound,sound1;

	@Override
	public void create () {
		batch = new SpriteBatch();
		backGround = new Texture("background.png");
		enemy1 = new Texture("enemy1.png");
		enemy2 = new Texture("enemy1.png");
		enemy3 = new Texture("enemy1.png");
		bird = new Texture("bird.png");
		birdWidth = Gdx.graphics.getWidth()/13;
		birdHeight = Gdx.graphics.getHeight()/8;
		skullWidth = Gdx.graphics.getWidth()/10;
		skullHeight = Gdx.graphics.getHeight()/8;
		birdX = Gdx.graphics.getWidth()/4;
		birdY = Gdx.graphics.getHeight();
		backGroundWidth = Gdx.graphics.getWidth();
		backGroundHeight = Gdx.graphics.getHeight();
		enemyX = backGroundWidth;

		sound = Gdx.audio.newSound(Gdx.files.internal("lose.wav"));
		sound1 = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		c_bird = new Circle();
		c_enemy1 = new Circle[numEnemies];
		c_enemy2 = new Circle[numEnemies];
		c_enemy3 = new Circle[numEnemies];

		font = new BitmapFont();
		font.setColor(Color.PINK);
		font.getData().setScale(8);

		font1 = new BitmapFont();
		font1.setColor(Color.GOLDENROD);
		font1.getData().setScale(10);

		font2 = new BitmapFont();
		font2.setColor(Color.SALMON);
		font2.getData().setScale(10);

		for(int i=0; i<numEnemies; i++){
			enemiesX[i] = backGroundWidth + i *backGroundWidth/2;
			Random r1 = new Random();
			Random r2 = new Random();
			Random r3 = new Random();

			enemiesY[0][i] = r1.nextFloat() * backGroundHeight;
			enemiesY[1][i] = r2.nextFloat() * backGroundHeight;
			enemiesY[2][i] = r3.nextFloat() * backGroundHeight;

			c_enemy1[i] = new Circle();
			c_enemy2[i] = new Circle();
			c_enemy3[i] = new Circle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(backGround,0,0,backGroundWidth ,backGroundHeight);
		batch.draw(bird,birdX ,birdY ,birdWidth,birdHeight);
		if(state==1) {
			
			if (Gdx.input.justTouched()) {
				velocity = -15;
				sound1.play();
			}
			flag1 = true;
			for(int i =0;i<numEnemies;i++){
				if(enemiesX[i]<0){
					flag=true;
					enemiesX[i] = numEnemies*backGroundWidth/2;
					Random r1 = new Random();
					Random r2 = new Random();
					Random r3 = new Random();

					enemiesY[0][i] = r1.nextFloat() * backGroundHeight;
					enemiesY[1][i] = r2.nextFloat() * backGroundHeight;
					enemiesY[2][i] = r3.nextFloat() * backGroundHeight;
				}
				font.draw(batch, String.valueOf(score), backGroundWidth-birdWidth, birdHeight);
				if(birdX>enemiesX[i] &&flag){
					score++;
					flag=false;
				}

				enemiesX[i]=enemiesX[i] - 10;
				batch.draw(enemy1,enemiesX[i], enemiesY[0][i],skullWidth,skullHeight);
				batch.draw(enemy2,enemiesX[i], enemiesY[1][i], skullWidth,skullHeight);
				batch.draw(enemy3,enemiesX[i], enemiesY[2][i], skullWidth,skullHeight);

			}
			if (birdY < birdHeight/1.2) {
				birdY = backGroundWidth / 3;
				velocity = 0;
				state = 2;
			} else {
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			}
		}
		else if(state==2) {
			font2.draw(batch,"You lost! Tap the screen to try again!",birdWidth/2, backGroundHeight/2+ birdWidth);
			if(flag1) {
				sound.play();
				flag1 =false;
			}
			font.draw(batch, String.valueOf(score), backGroundWidth-birdWidth, birdHeight);
			if (Gdx.input.justTouched()) {
				birdX = Gdx.graphics.getWidth()/4;
				birdY = Gdx.graphics.getHeight();
				score=0;
				for(int i=0; i<numEnemies; i++){
					enemiesX[i] = backGroundWidth + i *backGroundWidth/2;
					Random r1 = new Random();
					Random r2 = new Random();
					Random r3 = new Random();

					enemiesY[0][i] = r1.nextFloat() * backGroundHeight;
					enemiesY[1][i] = r2.nextFloat() * backGroundHeight;
					enemiesY[2][i] = r3.nextFloat() * backGroundHeight;

					c_enemy1[i] = new Circle();
					c_enemy2[i] = new Circle();
					c_enemy3[i] = new Circle();
				}
				state = 1;
			}
		}

		else if(state==0){
			font1.draw(batch,"Tap the screen to start!",backGroundWidth/4-birdWidth/2, backGroundHeight/2+ birdWidth);
			if(Gdx.input.justTouched()){
				state=1;
			}
		}
		c_bird.set(birdX + birdWidth/2,birdY + birdHeight/2,birdWidth/2);
		for(int i=0;i<numEnemies;i++){
			c_enemy1[i].set(enemiesX[i]+skullWidth/2, enemiesY[0][i]+skullHeight/2, skullHeight/2);
			c_enemy2[i].set(enemiesX[i]+skullWidth/2, enemiesY[1][i]+skullHeight/2, skullHeight/2);
			c_enemy3[i].set(enemiesX[i]+skullWidth/2, enemiesY[2][i]+skullHeight/2, skullHeight/2);
			if(Intersector.overlaps(c_bird,c_enemy1[i]) || Intersector.overlaps(c_bird,c_enemy2[i]) || Intersector.overlaps(c_bird,c_enemy3[i])){
				state = 2;
			}

		}
		batch.end();
	}
	
	@Override
	public void dispose(){

	}
}
