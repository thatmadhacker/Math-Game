package entities;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.Loader;

public class Lamp extends Entity {
	Light light;
	List<Light> lights;
	public Lamp(Vector3f position, float scale, Loader loader, List<Light> lights, Vector3f pos, Vector3f colour, Vector3f attenuation) {
		super(loader.createTexturedModel("lamp", "lamp", "png", 0, 0, true, true), position, 0, 0, 0, scale,
				EntityType.LAMP);
		setPosition(pos);
		light = new Light(new Vector3f(pos.x,(float) (pos.y+13.8),pos.z),colour, attenuation);
		lights.add(light);
		this.lights = lights;
	}

	public void update() {
		light.setPosition(new Vector3f(getPosition().x,(float) (getPosition().y+13.8),getPosition().z));
	}
	public void setColour(Vector3f colour){
		light.setColour(colour);
	}
	public void remove(){
		lights.remove(light);
	}

}
