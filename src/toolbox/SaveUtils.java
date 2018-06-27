package toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import entities.Entity;
import entities.EntityType;
import entities.Lamp;
import entities.Light;
import entities.Light.LightType;
import entities.Player;
import renderEngine.Loader;
import terrain.Terrain;

public class SaveUtils {
	
	public static void loadSave(File loc, List<Entity> entities, List<Light> lights, List<Terrain> terrains, Player player, Loader loader){
		Scanner in = null;
		try {
			in = new Scanner(loc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(in.hasNextLine()){
			String line = in.nextLine();
			if(line.startsWith("player:")){
				line = line.substring(7);
				String[] data = line.split(" ");
				Vector3f position = new Vector3f(Float.valueOf(data[0]),Float.valueOf(data[1]),Float.valueOf(data[2]));
				Vector3f rotation = new Vector3f(Float.valueOf(data[3]),Float.valueOf(data[4]),Float.valueOf(data[5]));
				player = new Player(loader.createTexturedModel("person", "playerTexture", "png", 0, 0, false, false), position, rotation.x, rotation.y, rotation.z, 1);
			}
			if(line.startsWith("seed:")){
				line = line.substring(5);
				MainGameLoop.seed = Integer.valueOf(line);
			}
			if(line.startsWith("light:")){
				line = line.substring(6);
				String[] data = line.split(" ");
				Vector3f position = new Vector3f(Float.valueOf(data[0]),Float.valueOf(data[1]),Float.valueOf(data[2]));
				Vector3f colour = new Vector3f(Float.valueOf(data[3]),Float.valueOf(data[4]),Float.valueOf(data[5]));
				Vector3f attenuation = new Vector3f(Float.valueOf(data[6]),Float.valueOf(data[7]),Float.valueOf(data[8]));
				lights.add(new Light(position,colour,attenuation));
			}
			if(line.startsWith("sun:")){
				line = line.substring(4);
				String[] data = line.split(" ");
				Vector3f position = new Vector3f(Float.valueOf(data[0]),Float.valueOf(data[1]),Float.valueOf(data[2]));
				Vector3f colour = new Vector3f(Float.valueOf(data[3]),Float.valueOf(data[4]),Float.valueOf(data[5]));
				lights.add(new Light(position,colour));
			}
			if(line.startsWith("entity:")){
				line = line.substring(7);
				String[] data = line.split(" ");
				EntityType type = EntityType.valueOf(data[0]);
				if(type == EntityType.LAMP){
					Vector3f position = new Vector3f(Float.valueOf(data[9]),Float.valueOf(data[10]),Float.valueOf(data[11]));
					Lamp l = new Lamp(position, 1, loader, lights, position, new Vector3f(2,2,0), new Vector3f(1,0.01f,0.002f));
					entities.add(l);
				}else{
					if(data.length < 12){
						break;
					}
					String objLoc = data[1];
					String textureLoc = data[2];
					String fileExtension = data[3];
					int numberOfRows = Integer.valueOf(data[4]);
					int scale = Float.valueOf(data[5]).intValue();
					Vector3f rotation = new Vector3f(Float.valueOf(data[6]),Float.valueOf(data[7]),Float.valueOf(data[8]));
					Vector3f position = new Vector3f(Float.valueOf(data[9]),Float.valueOf(data[10]),Float.valueOf(data[11]));
					Entity entity = new Entity(loader.createTexturedModel(objLoc, textureLoc,fileExtension, Float.valueOf(data[12]).intValue(), Float.valueOf(data[11]).intValue(), Boolean.valueOf(data[13]), Boolean.valueOf(data[14]),numberOfRows), position, rotation.x, rotation.y, rotation.z, scale, type);
					entities.add(entity);
				}
			}
		}
	}
	public static void save(File loc, List<Entity> entities, List<Light> lights, Player player){
		if(!loc.getParentFile().exists()){
			loc.getParentFile().mkdirs();
		}
		if(loc.exists()){
			loc.delete();
		}
		try {
			loc.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = new PrintWriter(loc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("seed:"+MainGameLoop.seed);
		out.println("player:"+player.getPosition().x + " " + player.getPosition().y + " "+ player.getPosition().z +" "+player.getRotX() + " "+player.getRotY() + " "+player.getRotZ());
		for(Light l : lights){
			if(l.getType() == LightType.LAMP){
				out.println("light:"+l.getPosition().x+" "+l.getPosition().y+" "+l.getPosition().z+" "+l.getColour().x+" "+l.getColour().y+" "+l.getColour().z+" "+l.getAttenuation().x+" "+l.getAttenuation().y+" "+l.getAttenuation().z);
				out.flush();
			}else{
				out.println("sun:"+l.getPosition().x+" "+l.getPosition().y+" "+l.getPosition().z+" "+l.getColour().x+" "+l.getColour().y+" "+l.getColour().z);
				out.flush();
			}
		}
		for(Entity e : entities){
			out.println("entity:"+e.getType()+" "+e.getModel().getRawModel().getLoc()+" "+e.getModel().getTexture().getLoc()+ " "+ e.getModel().getTexture().getExtension() + " " + e.getModel().getTexture().getNumberOfRows()+" "+e.getScale()+" "+e.getRotX()+" "+e.getRotY()+" "+e.getRotZ()+" "+e.getPosition().x+" "+e.getPosition().y+" "+e.getPosition().z+" "+ e.getModel().getTexture().getReflectivity()+" "+e.getModel().getTexture().getShineDamper()+" "+e.getModel().getTexture().isTransparent()+ " "+ e.getModel().getTexture().useFakeLighting());
			out.flush();
		}
		out.flush();
		out.close();
	}
}
