package tj.test.asm;

public class DemoWeaver extends DemoStub {
	public static String FIELD_STR_DEFAULT = "织入后的#FIELD_STR_DEFAULT";
	private String weaverAddField = "weaverAddField";
	public String privStringField = "织入后的#privStringField";
	/**
	* 织入添加方法
	* 
	* @return
	*/
	private String weaverAddMethod() {
	System.out.println("Do weaverAddMethod");
	return weaverAddField;
	}
	/**
	* 替换目标类的privMethod方法
	*/
	public void privMethod() {
	System.out.println(weaverAddMethod());// 调用weaverAddMethod方法并打印返回结果
	System.out.println("**调用目标类原privMethod方法--begin");
	super.privMethod();// 调用目标类原privMethod方法
	System.out.println("**调用目标类原privMethod方法--end");
	System.out.println("打印目标类protStringField字段值：" + protStringField);// 访问目标类字段protStringField
	}
	/**
	* 替换目标类的staticPrivMethod方法
	*/
	public static void staticPrivMethod() {
	System.out
	.println("This is weaver static PrivMethod //织入后的打印#staticPrivMethod");
	}
	/**
	* 替换目标类的publishMethod方法
	*/
	public void publishMethod() {
	System.out
	.println("This is weaver static PrivMethod //织入后的打印#publishMethod");
	}
}