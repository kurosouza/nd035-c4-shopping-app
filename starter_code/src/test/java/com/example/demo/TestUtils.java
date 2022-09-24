package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

	public static void injectObjects(Object target, String fieldName, Object objToInject) {
		boolean wasPrivate = false;
		
		try {
			Field f = target.getClass().getDeclaredField(fieldName);
			
			if(!f.isAccessible()) {
				f.setAccessible(true);
				wasPrivate = true;
			}
			f.set(target, objToInject);
			if(wasPrivate) {
				f.setAccessible(false);
			}
		} catch(NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
