package textures;

public class ModelTexture {

	private int textureID;
	private boolean hasTransparency;
	private float shineDamper;
	private boolean useFakeLighting = false;
	private String loc;
	private String extension;
	
	private int numberOfRows = 1;
	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	private float reflectivity;
	public ModelTexture(int id, float shineDamper, float reflectivity, boolean hasTransparency, boolean useFakeLighting, String loc, String extension){
		this.textureID = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.hasTransparency = hasTransparency;
		this.useFakeLighting = useFakeLighting;
		this.loc = loc;
		this.extension = extension;
	}
	public ModelTexture(int id, float shineDamper, float reflectivity, boolean hasTransparency, boolean useFakeLighting, int numberOfRows, String loc, String extension){
		this.textureID = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.hasTransparency = hasTransparency;
		this.useFakeLighting = useFakeLighting;
		this.numberOfRows = numberOfRows;
		this.loc = loc;
		this.extension = extension;
	}
	public String getExtension() {
		return extension;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isTransparent() {
		return hasTransparency;
	}

	public String getLoc() {
		return loc;
	}

	public void setTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getTextureID() {
		return textureID;
	}

	public boolean useFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
}
