package tj.test.asm;

import java.io.IOException;
/**
* @author zp
* @mail zherop@163.com
* @date 2016年5月6号
*/
public class TestClassLoader extends ClassLoader {
	public TestClassLoader() {
		super();
	}
	public static Class<?> testWeaver(final String targetClassName,
		final String targetStubClsName, final String weaverClassName) {
		final TestClassLoader cl = new TestClassLoader();
		ClassWeaver classWeaver = new ClassWeaver(targetClassName,
		targetStubClsName, weaverClassName);
		try {
		classWeaver.weaver();
		classWeaver.write2File("E:/asm/" + targetClassName + ".class");
		} catch (IOException e) {
		e.printStackTrace();
		}
		byte[] classBytes = classWeaver.getClassBytes();
		try {
		Class<?> newClass = cl.defineClass(targetClassName, classBytes, 0,
		classBytes.length);
		return newClass;
		} catch (SecurityException e) {
		e.printStackTrace();
		} catch (IllegalArgumentException e) {
		e.printStackTrace();
		}
		return null;
	}
}


	
	
