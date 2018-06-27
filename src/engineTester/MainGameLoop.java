package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.EntityType;
import entities.Follower;
import entities.Lamp;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiTexture;
import models.Hitbox;
import models.TexturedModel;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.GuiRenderer;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Height;
import terrain.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import toolbox.SaveUtils;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {
	
	private static int selected = 0;
	public static int seed;
	public static List<Entity> entities;
	public static void main(String[] args) {
		Random random = new Random();
		seed = random.nextInt(1000000000);
		
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		
		TextMaster.init(loader);
		MasterRenderer renderer = new MasterRenderer(loader);
		
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("fire", "png"),8);
		ParticleSystem ps = new ParticleSystem(particleTexture,40,10, 0.1f, 1,4f);
		
		List<Light> lights = new ArrayList<Light>();
		//Light sun = new Light(new Vector3f(1000000,1500000,-1000000),new Vector3f(0.4f, 0.4f, 0.4f));
		//lights.add(sun);
		//lights.add(new Light(new Vector3f(185,10,-293),new Vector3f(2, 0, 0), new Vector3f(1,0.01f,0.002f)));
		//lights.add(new Light(new Vector3f(370,17,-300),new Vector3f(0, 2, 2), new Vector3f(1,0.01f,0.002f)));
		//lights.add(new Light(new Vector3f(293,7,-305),new Vector3f(2, 2, 0), new Vector3f(1,0.01f,0.002f)));
		
		entities = new ArrayList<Entity>();
		
		//entities.add(new Entity(lamp, new Vector3f(500, -4.7f, -500),0,0,0,1,EntityType.LAMP));
		//entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300),0,0,0,1,EntityType.LAMP));
		//entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -305),0,0,0,1,EntityType.LAMP));
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		Player player = new Player(loader.createTexturedModel("person", "playerTexture", "png", 0, 0, false, false), new Vector3f(185, 0, -293), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", "png"));
		TerrainTexture background = new TerrainTexture(loader.loadTexture("grass", "png"));
		TerrainTexture r = new TerrainTexture(loader.loadTexture("mud", "png"));
		TerrainTexture g = new TerrainTexture(loader.loadTexture("flower", "png"));
		TerrainTexture b = new TerrainTexture(loader.loadTexture("path", "png"));
		ArrayList<Height> heights = new ArrayList<Height>();
		TerrainTexturePack terrainPack = new TerrainTexturePack(background, r, g, b);
		terrains.add(new Terrain(0, 0, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(1, 0, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(0, 1, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(1, 1, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(-1, 0, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(0, -1, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(-1, -1, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(1, -1, loader, terrainPack, blendMap, "heightmap", heights));
		terrains.add(new Terrain(-1, 1, loader, terrainPack, blendMap, "heightmap", heights));
		File saveFile = new File("saves/save.save");
		if(saveFile.exists()){
			SaveUtils.loadSave(saveFile, entities, lights, terrains, player, loader);
		}else{
			for(Terrain terrain : terrains){
				terrain.populate(loader, entities);
			}
			Light sun = new Light(new Vector3f(1000000,1500000,-1000000),new Vector3f(0.4f, 0.4f, 0.4f));
			lights.add(sun);
		}
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		//GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan", "png"),new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f));
		//guis.add(gui);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix());
		
		FontType font = new FontType(loader.loadTexture("arial","png"),"/res/arial.fnt");
		GUIText position = new GUIText("x: 0 y: 0 z: 0", 1,font, new Vector2f(0f,0f),0.5f, true);
		position.setColour(1, 0, 0);
		ps.randomizeRotation();
		ps.setDirection(new Vector3f(0,1,0), 0.1f);
		ps.setLifeError(0.1f);
		ps.setSpeedError(0.4f);
		ps.setScaleError(0.8f);
		TexturedModel treeModel = loader.createTexturedModel("tree", "tree", "png", 0, 0, false, false);
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader,waterShader,renderer.getProjectionMatrix());
		List<WaterTile> waters = new ArrayList<WaterTile>();
		//WaterTile water = new WaterTile(75,-75,0);
		//waters.add(water);
		/*WaterFrameBuffers buffers = new WaterFrameBuffers();
		GuiTexture refraction = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(0.5f,0.5f), new Vector2f(0.25f, 0.25f));
		GuiTexture reflection = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.5f,0.5f), new Vector2f(0.25f,0.25f));
		guis.add(reflection);
		guis.add(refraction);*/
		//box = loader.createTexturedModel("box", "box", "box", 0, 0, false, false);
		
		while (!DisplayManager.isCloseRequested()) {
			Terrain playerTerrain = getPlayerTerrain(player, terrains);
			
			move(player, camera, entities, loader, playerTerrain);
			
			//System.out.println("X: "+player.getPosition().getX()+" Y: "+player.getPosition().getY()+" Z: "+player.getPosition().getZ());
			
			position.setText("x: "+Math.floor(player.getPosition().x)+" y: "+Math.floor(player.getPosition().y)+" z: "+Math.floor(player.getPosition().z));
			
			pick(picker, playerTerrain, treeModel, entities, camera,loader, lights,player);
			
			/*GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.setPitch(-camera.getPitch());
			render(renderer, player, entities, terrains, lights, camera,new Vector4f(0,1,0,0));
			camera.getPosition().y+= distance;
			camera.setPitch(-camera.getPitch());
			buffers.bindRefractionFrameBuffer();
			render(renderer, player, entities, terrains, lights, camera,new Vector4f(0,1,0,-5));
			
			buffers.unbindCurrentFrameBuffer();
			*/
			render(renderer, player, entities, terrains, lights, camera,new Vector4f(0,-1,0,10000));
			
			renderWater(waterRenderer, waters, camera);
			
			//particles(player, camera, ps);
			
			guiRenderer.render(guis);
			
			TextMaster.render();
			
			DisplayManager.updateDisplay();
		}
		
		cleanUp(renderer, loader, guiRenderer, waterShader, entities, lights, player);
		
		DisplayManager.closeDisplay();
	}
	
	private static void renderWater(WaterRenderer waterRenderer, List<WaterTile> waters, Camera camera) {
		waterRenderer.render(waters, camera);
	}

	private static void cleanUp(MasterRenderer renderer, Loader loader, GuiRenderer guiRenderer, WaterShader waterShader, List<Entity> entities, List<Light> lights, Player player){
		renderer.cleanUp();
		loader.cleanUp();
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();
		waterShader.cleanUp();
		SaveUtils.save(new File("saves/save.save"), entities, lights, player);
	}
	
	private static Terrain getPlayerTerrain(Player player, List<Terrain> terrains){
		for (Terrain terrain : terrains) {
			if (terrain.getX() <= player.getPosition().x) {
				if (terrain.getX() + Terrain.SIZE > player.getPosition().x) {
					if (terrain.getZ() <= player.getPosition().z) {
						if (terrain.getZ() + Terrain.SIZE > player.getPosition().z) {
							return terrain;
						}
					}
				}
			}
		}
		return null;
	}
	private static void move(Player player, Camera camera, List<Entity> entities, Loader loader, Terrain playerTerrain){
		player.move(playerTerrain, entities, player, loader);
		camera.move();
	}
	private static void pick(MousePicker picker, Terrain playerTerrain, TexturedModel treeModel, List<Entity> entities, Camera camera,Loader loader, List<Light> lights, Player player){
		if(Keyboard.isKeyDown(Keyboard.KEY_1)){
			selected = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)){
			selected = 2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_3)){
			selected = 3;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_4)){
			selected = 4;
		}
		picker.update(playerTerrain);
		Vector3f terrainPoint = picker.getCurrentTerrainPoint();
		Vector3f ray = picker.getCurrentRay();
		List<Vector3f> points = new ArrayList<Vector3f>();
		for(int i = 0; i < MousePicker.RAY_RANGE;i++){
			Vector3f point = MousePicker.getPointOnRay(ray, i, camera);
			points.add(point);
		}
		List<Vector3f> stuff = new ArrayList<Vector3f>();
		for(Entity e : entities){
			Hitbox hitbox = e.getModel().getRawModel().getHitbox();
			Vector3f position = e.getPosition();
			for(Vector3f point : points){
				if(isInHitbox(point, position, hitbox)){
					stuff.add(point);
					break;
				}
			}
		}
		double lowestZ = Integer.MAX_VALUE;
		Vector3f lowest = null;
		for(int i = 0; i < stuff.size();i++){
			Vector3f point = stuff.get(i);
			double distance;
			float xDiff = camera.getPosition().x - point.x;
			float yDiff = camera.getPosition().y - point.y;
			distance = Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2));
			if(distance < lowestZ){
				lowestZ = distance;
				lowest = point;
			}
		}
		if(lowest != null){
			terrainPoint = lowest;
		}
		if(terrainPoint != null){
			if(Mouse.isButtonDown(0)){
				for(Entity e : entities){
					Hitbox hitbox = e.getModel().getRawModel().getHitbox();
					Vector3f position = e.getPosition();
					for(Vector3f point : points){
						if(isInHitbox(point, position, hitbox)){
							if(e.getType() == EntityType.DOOR){
								if(!e.active){
									e.setRotY(e.getRotY() + 90);
									e.active = true;
								}else{
									e.setRotY(e.getRotY() - 90);
									e.active = false;
								}
								return;
							}
						}
					}
				}
				if(selected == 1){
					Entity e = new Entity(loader.createTexturedModel("tree", "tree", "png", 0, 0, false, false), new Vector3f(terrainPoint), 0,0,0,5,EntityType.TREE);
					entities.add(e);
				}else if(selected == 2){
					Entity e = new Entity(loader.createTexturedModel("box", "box", "png", 0, 0, false, false), new Vector3f(terrainPoint.x,terrainPoint.y+3,terrainPoint.z), player.getRotX(),player.getRotY(),player.getRotZ(),5,EntityType.OTHER);
					entities.add(e);
				}else if(selected == 3){
					Lamp l = new Lamp(new Vector3f(terrainPoint), 1, loader, lights, new Vector3f(terrainPoint.x,terrainPoint.y,terrainPoint.z), new Vector3f(2,2,0), new Vector3f(1,0.01f,0.002f));
					entities.add(l);
				}else if(selected == 4){
					Follower f = new Follower(loader.createTexturedModel("person", "follower", "png", 0, 0, false, false),new Vector3f(player.getPosition()), EntityType.OTHER, player);
					entities.add(f);
				}
			}
			if(Mouse.isButtonDown(1)){
				Entity entity = null;
				for(Entity e : entities){
					Hitbox hitbox = e.getModel().getRawModel().getHitbox();
					Vector3f position = e.getPosition();
					for(Vector3f point : points){
						if(isInHitbox(point, position, hitbox)){
							entity = e;
							break;
						}
					}
				}
				if(entity instanceof Lamp){
					((Lamp) entity).remove();
				}
				entities.remove(entity);
			}
		}
		
	}
	private static void render(MasterRenderer renderer, Player player, List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane){
		renderer.processEntity(player);
		for (Entity e : entities) {
			if(e instanceof Follower){
				((Follower) e).update(player, terrains);
			}
			renderer.processEntity(e);
		}
		for (Terrain t : terrains) {
			renderer.processTerrain(t);
		}
		renderer.render(lights, camera,clipPlane);
	}
	
	@SuppressWarnings("unused")
	private static void particles(Player player, Camera camera, ParticleSystem ps){
		ps.generateParticles(new Vector3f(player.getPosition()));
		ParticleMaster.update(camera);
		ParticleMaster.renderParticles(camera);
	}
	public static boolean isInHitbox(Vector3f point, Vector3f entityPos, Hitbox hitbox){
		if(point.x > entityPos.x && point.x < entityPos.x + hitbox.getWidth()){
			if(point.y > entityPos.y && point.y < entityPos.y + hitbox.getHeight()){
				if(point.z > entityPos.z && point.z < entityPos.z + hitbox.getDepth()){
					return true;
				}
			}
		}
		return false;
	}
}
