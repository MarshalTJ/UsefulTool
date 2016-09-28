package tj.test.asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tj.test.asm.TestClassLoader;
public class Test {
	
	public static void main(String[] args) throws SecurityException,
													NoSuchMethodException, InstantiationException,
													IllegalAccessException, IllegalArgumentException,
													InvocationTargetException {
		String targetClassName = DemoTarget.class.getName();
		String targetStubClsName = DemoStub.class.getName();
		String weaverClassName = DemoWeaver.class.getName();
		Class<?> newClass = TestClassLoader.testWeaver(targetClassName,
				targetStubClsName, weaverClassName);
		if (newClass == null) {
		System.out.println("类生成失败");
		return;
		}
		Object object = newClass.newInstance();
		Method printAllField = newClass.getDeclaredMethod("printAllField",
		new Class<?>[] {});
		printAllField.invoke(object, new Object[] {});
		Method doAllMethod = newClass.getDeclaredMethod("doAllMethod",
		new Class<?>[] {});
		doAllMethod.invoke(object, new Object[] {});
	}
}