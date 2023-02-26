package com.greyog.lifegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LifeGame extends ApplicationAdapter {

	final public static int worldWidth = 250;
	final public static int worldHeight = 250;

	SpriteBatch batch;
	Texture img;

	ShapeRenderer renderer;
	OrthographicCamera camera;
	FitViewport viewport;

	float pointSize;
	float drawOffset;

	Field field;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		camera = new OrthographicCamera();
		viewport = new FitViewport(worldWidth, worldHeight, camera);
		renderer = new ShapeRenderer();

		pointSize = 2f / Math.max(worldHeight, worldWidth);
		drawOffset = 1f;

		field = new Field(worldWidth, worldHeight);

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		renderer.setProjectionMatrix(camera.combined);

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		for (int i = 0; i < worldWidth; i++) {
			for (int j = 0; j < worldHeight; j++) {
				renderer.setColor(field.getColorAt(i, j));
				renderer.rect(i * pointSize - 1, j * pointSize - 1, pointSize, pointSize );
			}
		}
		renderer.end();

		field.generateNext();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		renderer.dispose();
	}
}
