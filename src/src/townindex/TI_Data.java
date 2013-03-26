package src.townindex;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

@SerializableAs("TI_Data")
public class TI_Data implements ConfigurationSerializable{
	
	private String tname;
	private String tstatus;
	private String mayor;
	private String vmayor;
	private String wing;
	private Vector location;
	private String pavail;
	
	public TI_Data(String tname) {
		this.tname = tname;
	}
	
	public String getTName() {
		return tname;
	}
	
	public void setTStatus(String tstatus) {
		this.tstatus = tstatus;
	}
	
	public String getTStatus() {
		return tstatus;
	}
	
	public void setName(String tname) {
		this.tname = tname;
	}
	
	public String getMayor() {
		return mayor;
	}
	
	public void setMayor(String mayor) {
		this.mayor = mayor;
	}
	
	public String getVMayor() {
		return vmayor;
	}
	
	public void setVMayor(String vmayor) {
		this.vmayor = vmayor;
	}
	
	
	public String getWing() {
		return wing;
	}
	
	public void setWing(String wing) {
		this.wing = wing;
	}
	
	public Vector getLocation() {
		return location;
	}
	
	public void setLocation(Vector location) {
		this.location = location;
	}
	
	public String getPAvail() {
		return pavail;
	}
	
	public void setPAvail(String pavail) {
		this.pavail = pavail;
	}
	
	@Override
	public Map<String, Object> serialize() {
		 Map<String, Object> tmap = new LinkedHashMap<String, Object>();

		 tmap.put("tname", tname);
		 tmap.put("tstatus", tstatus);
		 tmap.put("mayor", mayor);
		 tmap.put("vmayor", vmayor);
		 tmap.put("wing", wing);
		 tmap.put("location", location);
		 tmap.put("pavail", pavail);

		 return tmap;
	}

	public static TI_Data deserialize(Map<String, Object> tmap) {
		String tstatus = "";
		String mayor = "";
		String vmayor = "";
		String wing = "";
		Vector location = new Vector();
		String pavail = "";

		if (!tmap.containsKey("tname")) return null;
		String tname = tmap.get("tname").toString();
		
		if (tmap.get("tstatus") != null) tstatus = tmap.get("tstatus").toString();
		if (tmap.get("mayor") != null) mayor = tmap.get("mayor").toString();
		if (tmap.get("vmayor") != null) vmayor = tmap.get("vmayor").toString();
		if (tmap.get("wing") != null) wing = tmap.get("wing").toString();
		if (tmap.get("location") != null) location = (Vector) tmap.get("location");
		if (tmap.get("pavail") != null) pavail = tmap.get("pavail").toString();
		
		TI_Data tmapfinish = new TI_Data(tname);
		tmapfinish.tname = tname;
		tmapfinish.tstatus = tstatus;
		tmapfinish.mayor = mayor;
		tmapfinish.vmayor = vmayor;
		tmapfinish.wing = wing;
		tmapfinish.location = location;
		tmapfinish.pavail = pavail;
		return tmapfinish;
	}
}