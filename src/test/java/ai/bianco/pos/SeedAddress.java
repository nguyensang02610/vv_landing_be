package ai.bianco.pos;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.vvlanding.service.AddressService;
import com.vvlanding.table.AddressDistrict;
import com.vvlanding.table.AddressProvince;
import com.vvlanding.table.AddressWard;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.Map.Entry;

@SpringBootTest
public class SeedAddress {
	Gson gson = new Gson();
	@Autowired
	private AddressService addressService;

//	@Test
	public void getAllAddress() {
		List<AddressProvince> all = addressService.getAll();
		System.out.println(gson.toJson(all));
	}

	@Test
	public void insertAddress() {
		try {
			Workbook workbook = WorkbookFactory.create(new File("data/Province-District-Ward.xls"));
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter dataFormatter = new DataFormatter();

			Map<String, AddressProvince> mapIdProvince = new LinkedHashMap<String, AddressProvince>();
			Map<String, LinkedHashSet<String>> mapProvinceDistrict = new LinkedHashMap<String, LinkedHashSet<String>>();

			Map<String, AddressDistrict> mapIdDistrict = new LinkedHashMap<String, AddressDistrict>();
			Map<String, List<AddressWard>> mapDistrictWards = new LinkedHashMap<String, List<AddressWard>>();

			Iterator<Row> rowIterator = sheet.rowIterator();
			int cnt = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				cnt++;
				if (cnt == 1)
					continue;

				String provinceName = dataFormatter.formatCellValue(row.getCell(0));
				String provinceID = dataFormatter.formatCellValue(row.getCell(1));
				String provinceCode = dataFormatter.formatCellValue(row.getCell(2));
				String districtName = dataFormatter.formatCellValue(row.getCell(3));
				String districtCode = dataFormatter.formatCellValue(row.getCell(4));
				String districtID = dataFormatter.formatCellValue(row.getCell(5));

				if (!mapIdDistrict.containsKey(districtID)) {
					AddressDistrict addressDistrict = new AddressDistrict(districtID, districtName, districtCode, null);
					mapIdDistrict.put(districtID, addressDistrict);
				}

				if (!mapIdProvince.containsKey(provinceID)) {
					AddressProvince addressProvince = new AddressProvince(provinceID, provinceName, provinceCode, null);
					mapIdProvince.put(provinceID, addressProvince);
				}

				LinkedHashSet<String> districtList = mapProvinceDistrict.get(provinceID);
				if (districtList == null) {
					districtList = new LinkedHashSet<String>();
				}
				districtList.add(districtID);
				mapProvinceDistrict.put(provinceID, districtList);
			}

			JsonReader reader = new JsonReader(new FileReader("data/wards.json"));
			WardObject[] wardArrays = gson.fromJson(reader, WardObject[].class);

			for (WardObject ward : wardArrays) {
				String wardName = ward.getWardName();
				String wardCode = ward.getWardCode();
				String districtID = String.valueOf(ward.getDistrictID());
				AddressWard addressWard = new AddressWard(wardCode, wardName, wardCode);
				List<AddressWard> wardsList = mapDistrictWards.get(districtID);
				if (wardsList == null) {
					wardsList = new ArrayList<AddressWard>();
				}
				wardsList.add(addressWard);
				mapDistrictWards.put(districtID, wardsList);
			}

			List<AddressProvince> addressProvinces = new ArrayList<AddressProvince>();
			for (Entry<String, AddressProvince> entry : mapIdProvince.entrySet()) {
				String provinceID = entry.getKey();
				AddressProvince province = entry.getValue();

				LinkedHashSet<String> districtSet = mapProvinceDistrict.get(provinceID);
				List<AddressDistrict> districts = new ArrayList<AddressDistrict>();
				for (String districtID : districtSet) {
					AddressDistrict addressDistrict = mapIdDistrict.get(districtID);
					List<AddressWard> wards = mapDistrictWards.get(districtID);
					addressDistrict.setWards(wards);

					districts.add(addressDistrict);
				}

				province.setDistricts(districts);
				addressProvinces.add(province);
			}
//			System.out.println(gson.toJson(addressProvinces));
			addressService.saveAll(addressProvinces);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
