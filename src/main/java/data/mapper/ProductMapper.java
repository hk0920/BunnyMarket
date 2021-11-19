package data.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import data.dto.ProductDTO;

@Mapper
public interface ProductMapper {
	public List<ProductDTO> getList(HashMap<String, Integer> map);
	public ProductDTO getData(String idx);
}
