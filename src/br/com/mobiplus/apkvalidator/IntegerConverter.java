package br.com.mobiplus.apkvalidator;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IntegerConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		IntegerRes integerRes = (IntegerRes) value;
		writer.addAttribute("name", integerRes.getName());
		writer.setValue(integerRes.getValue());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		IntegerRes integerRes = new IntegerRes();
		integerRes.setName(reader.getAttribute("name"));
		integerRes.setValue(reader.getValue());
		return integerRes;
	}

	public boolean canConvert(Class clazz) {
		return clazz.equals(IntegerRes.class);
	}

}
