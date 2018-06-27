package terrain;

import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.EntityType;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {

	public static final float SIZE = 800;
	private HeightsGenerator generator;
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private float[][] heights;

	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack pack, TerrainTexture blendmap,
			String heightMap, List<Height> heights) {
		this.blendMap = blendmap;
		this.texturePack = pack;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap, heights);
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	private RawModel generateTerrain(Loader loader, String heightMap, List<Height> heightsList) {
		generator = new HeightsGenerator();
		int VERTEX_COUNT = 128;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {

				float height = getHeight(j, i, generator);
				heights[j][i] = height;
				heightsList.add(new Height(j * (x * SIZE), i * (z * SIZE), height));

				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = getHeight(j, i, generator);
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, generator);
				normals[vertexPointer * 3] = normal.x; // normal.x
				normals[vertexPointer * 3 + 1] = normal.y; // normal .y
				normals[vertexPointer * 3 + 2] = normal.z; // normal.z
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices, null,"");
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return answer;

	}

	private float getHeight(int x, int z, HeightsGenerator generator) {
		return generator.generateHeight(x, z);
	}

	private Vector3f calculateNormal(int x, int z, HeightsGenerator generator) {
		float heightL = getHeight(x - 1, z, generator);
		float heightR = getHeight(x + 1, z, generator);
		float heightD = getHeight(x, z - 1, generator);
		float heightU = getHeight(x, z + 1, generator);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	public void populate(Loader loader, List<Entity> entities) {
		TexturedModel treeModel = loader.createTexturedModel("tree", "tree", "png", 0, 0, false, false);
		//TexturedModel grassModel = loader.createTexturedModel("grassModel", "grassTexture", "png", 0, 0, true, true);
		TexturedModel fernModel = loader.createTexturedModel("fern", "fern", "png", 0, 0, true, false, 2);
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			float x = 0;
			float z = 0;
			x = (random.nextFloat() * 800) + this.x;
			z = (random.nextFloat() * 800) + this.z;
			if (getHeightOfTerrain(x, z) > 0) {
				entities.add(new Entity(treeModel, new Vector3f(x, getHeightOfTerrain(x, z), z), 0, 0, 0, 5,
						EntityType.TREE));
			}
			/*x = (random.nextFloat() * 800) + this.x;
			z = (random.nextFloat() * 800) + this.z;
			if (getHeightOfTerrain(x, z) > 0) {
				entities.add(new Entity(grassModel, new Vector3f(x, getHeightOfTerrain(x, z), z), 0, 0, 0, 1,
						EntityType.GRASS));
			}*/
			x = (random.nextFloat() * 800) + this.x;
			z = (random.nextFloat() * 800) + this.z;
			if (getHeightOfTerrain(x, z) > 0) {
				entities.add(new Entity(fernModel, new Vector3f(x, getHeightOfTerrain(x, z), z), 0, 0, 0, 0.6f,
						EntityType.FERN, random.nextInt(4)));
			}
		}
	}
}
