package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import shaders.TerrainShader;
import terrain.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class TerrainRenderer {
	
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	public void render(List<Terrain> terrains){
		for(Terrain terrain : terrains){
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT,0);
			unbindTerrain();
		}
	}
	private void prepareTerrain(Terrain terrain){
		RawModel model = terrain.getModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		shader.longShineVariables(0, 0);
		bindTextures(terrain);
	}
	private void bindTextures(Terrain terrain){
		TerrainTexturePack texturePack = terrain.getTexturePack();
		TerrainTexture blendMap = terrain.getBlendMap();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, blendMap.getTextureID());
	}
	private void unbindTerrain(){
		GL20.glDisableVertexAttribArray(6);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	private void loadModelMatrix(Terrain terrain){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
