package ai.bianco.pos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WardObject {
	private String WardCode;
	private String WardName;
	private String DistrictCode;
	private int ProvinceID;
	private int DistrictID;
}
