package tj.test.asm;


public class DemoTarget {
	private static String FIELD_STR_DEFAULT = "Target_Default"; // 静态私有成员
	private String privStringField = FIELD_STR_DEFAULT; // 私有字段
	protected String protStringField = FIELD_STR_DEFAULT; // 保护字段
	String packStringField = FIELD_STR_DEFAULT; // 包作用域字段
	public String pubStringField = FIELD_STR_DEFAULT; // 公共字段
	/**
	* 私有方法
	*/
	private void privMethod() {
		staticPrivMethod();
		System.out
		.println("This is private method in Target Class!#privMethod");
	}
	/**
	* 静态私有方法
	*/
	private static void staticPrivMethod() {
		System.out
		.println("This is static private method in Target Class!#staticPrivMethod");
	}
	/**
	* 公共方法
	*/
	public void publishMethod() {
		System.out
		.println("This is publish method in Target Class!#publishMethod");
	}
	/**
	* 静态公共方法
	*/
	public static void staticPublishMethod() {
		System.out
		.println("This is static publish method in Target Class!#staticPublishMethod");
	}
	/**
	* 打印所有字段
	*/
	public void printAllField() {
		System.out.println("Print All Field --------------Begin");
		System.out.println(privStringField);
		System.out.println(protStringField);
		System.out.println(packStringField);
		System.out.println(pubStringField);
		System.out.println("Print All Field --------------End");
	}
	/**
	* 执行所有方法
	*/
	public void doAllMethod() {
		System.out.println("Do All Method --------------Begin");
		privMethod();
		staticPrivMethod();
		publishMethod();
		staticPublishMethod();
		System.out.println("Do All Method --------------End");
	}
}