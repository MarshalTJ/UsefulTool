package tj.test.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
/**
* @author zp
* @mail zherop@163.com
* @date 2016年5月6日
*/
public class ClassWeaver {
	private static String INTERNAL_INIT_METHOD_NAME = "";
	private static String INTERNAL_STSTIC_INIT_METHOD_NAME = "";
	private Map targetMethods;// 目标类方法信息<方法名，方法的访问权限>
	private List weaverMethodNames;// 织入类方法
	private List weaverFieldNames;// 织入类字段
	private final Map weaverInitFieldMap;// 织入类非静态字段初始化
	private final Map weaverStaticInitFieldMap;// 织入类静态字段初始化
	private ClassReader[] classReaders;
	private String targetStubClass; // 存根类
	private byte[] classBytes; // 生成的字节码
	
	public ClassWeaver(String targetClass, String targetStubClass,
		String weaverClass) {
		targetMethods = new HashMap();
		weaverMethodNames = new ArrayList();
		weaverFieldNames = new ArrayList();
		weaverInitFieldMap = new HashMap();
		weaverStaticInitFieldMap = new HashMap();
		setTargetStubClass(targetStubClass);
		initClassReaders(targetClass, weaverClass);
	}
	private void setTargetStubClass(String targetStubClsName) {
		if (targetStubClsName.contains(".")) {
		this.targetStubClass = targetStubClsName.replaceAll("//.", "/");
		} else {
		this.targetStubClass = targetStubClsName;
		}
	}
	private void initClassReaders(String targetClass, String weaverClass) {
		classReaders = new ClassReader[2];
		try {
		classReaders[0] = new ClassReader(targetClass);
		classReaders[1] = new ClassReader(weaverClass);
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
	/**
	* 初始化织入类的成员（属性、方法）
	* 
	* @throws IOException
	*/
	private void initWeaverMembers() throws IOException {
		ClassWriter cw = new ClassWriter(0);
		ClassReader cr = getWeaverClassReader();
		ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
			@Override
			public FieldVisitor visitField(int access, String name,
				String desc, String signature, Object value) {
				weaverFieldNames.add(name);
				return super.visitField(access, name, desc, signature, value);
			}
			@Override
			public MethodVisitor visitMethod(int access, String name,
				String desc, String signature, String[] exceptions) {
				MethodVisitor visitMethod = super.visitMethod(access, name,
				desc, signature, exceptions);
				// 对织入类静态字段的处理(获取字段的初始化值)
				if (INTERNAL_STSTIC_INIT_METHOD_NAME.equals(name)) {
				visitMethod = new FieldInitMethodVisitor(Opcodes.ASM5,
				visitMethod, weaverStaticInitFieldMap);
				}
				// 对织入类非静态字段的处理(获取字段的初始化值)
				else if (INTERNAL_INIT_METHOD_NAME.equals(name)) {
				visitMethod = new FieldInitMethodVisitor(Opcodes.ASM5,
				visitMethod, weaverInitFieldMap);
				} else {
				// 不是静态块，构造函数就保存起来
				weaverMethodNames.add(name);
				}
				return visitMethod;
			}
		};
		cr.accept(cv, 0);
	}
	/**
	* 初始化目标类的方法
	* 
	* @throws IOException
	*/
	private void initTargetMethods() throws IOException {
		ClassWriter cw = new ClassWriter(0);
		ClassReader cr = getTargetClassReader();
		ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
			@Override
			public MethodVisitor visitMethod(int access, String name,
			String desc, String signature, String[] exceptions) {
			MethodVisitor visitMethod = super.visitMethod(access, name,
			desc, signature, exceptions);
			if (!INTERNAL_INIT_METHOD_NAME.equals(name)
			&& !INTERNAL_STSTIC_INIT_METHOD_NAME.equals(name)) {
			targetMethods.put(name, access);
			}
			return visitMethod;
			}
		};
		cr.accept(cv, 0);
	}
	private ClassReader getWeaverClassReader() {
		return classReaders[1];
	}
	public void weaver() throws IOException {
		initTargetMethods();
		initWeaverMembers();
		ClassReader cr = getTargetClassReader();
		final ClassWriter cw = new ClassWriter(0);
		ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
			@Override
			public FieldVisitor visitField(int access, String name,
			String desc, String signature, Object value) {
			// 织入类包含该字段，则删除目标类中该字段
			if (weaverFieldNames.contains(name)) {
			return null;
			}
			return super.visitField(access, name, desc, signature, value);
			}
			@Override
			public MethodVisitor visitMethod(int access, String name,
				String desc, String signature, String[] exceptions) {
				// 对静态常量的处理
				if (INTERNAL_STSTIC_INIT_METHOD_NAME.equals(name)) {
				MethodVisitor visitMethod = super.visitMethod(access, name,
				desc, signature, exceptions);
				return new FieldInitValueSetMethodVisitor(Opcodes.ASM5,
				visitMethod, weaverStaticInitFieldMap,
				Opcodes.PUTSTATIC);
				}
				// 对目标类构造函数的处理（添加织入类中字段的初始化）
				else if (INTERNAL_INIT_METHOD_NAME.equals(name)) {
				MethodVisitor visitMethod = super.visitMethod(access, name,
				desc, signature, exceptions);
				return new FieldInitValueSetMethodVisitor(Opcodes.ASM5,
				visitMethod, weaverInitFieldMap, Opcodes.PUTFIELD);
				} else {
				// 若织入类覆盖了目标类的方法，则将目标类的该方法重命名
				if (weaverMethodNames.contains(name)) {
				name = name + "0";
				}
				return super.visitMethod(access, name, desc, signature,
				exceptions);
				}
			}
			@Override
			public void visitEnd() {
				ClassReader weaverClassReader = getWeaverClassReader();
				// 获取织入类的字段、方法
				ClassVisitor classVisitor = new WeaverClassVisitor(
				Opcodes.ASM5, cw);
				weaverClassReader.accept(classVisitor, 0);
				super.visitEnd();
			}
		};
		cr.accept(cv, 0);
		classBytes = cw.toByteArray();
	}
	private ClassReader getTargetClassReader() {
		return classReaders[0];
	}
	public byte[] getClassBytes() {
		return classBytes;
	}
	public void write2File(String output) throws IOException {
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(classBytes);
		if (fos != null) {
		fos.close();
		}
	}
	/**
	* 用于保存字段的初始化的信息
	*/
	class FieldInfo {
	String name;// 字段名称
	String desc;// 字段类型描述符
	String visitType;// 字段指令类型
	public FieldInfo(String name, String desc, String visitType) {
		this.name = name;
		this.desc = desc;
		this.visitType = visitType;
	}
	public String getName() {
		return name;
	}
	public String getDesc() {
		return desc;
	}
	public String getVisitType() {
		return visitType;
	}
	}
	/**
	* 访问字段的初始化值(目的：保存字段的初始化值)
	*/
class FieldInitMethodVisitor extends MethodVisitor {
	private Map output;
	private Object cstValue;
	private String type;
	public FieldInitMethodVisitor(int api, MethodVisitor mv,
		Map output) {
		super(api, mv);
		this.output = output;
	}
	@Override
	public void visitInsn(int opcode) {
		cstValue = opcode;
		type = "visitInsn";
		super.visitInsn(opcode);
	}
	@Override
	public void visitLdcInsn(Object cst) {
		cstValue = cst;
		type = "visitLdcInsn";
		super.visitLdcInsn(cst);
	}
	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
		FieldInfo fieldInfo = new FieldInfo(name, desc, type);
		output.put(fieldInfo, cstValue);
		super.visitFieldInsn(opcode, owner, name, desc);
	}
}
	/**
	* 字段初始化(目的：在生成的字节码中，设置字段的初始化值)
	*/
	class FieldInitValueSetMethodVisitor extends MethodVisitor {
	/**
	* fieldOpcode取值为Opcodes.PUTFIELD,Opcodes.PUTSTATIC
	*/
	private int fieldOpcode;
	private Map initFieldMap;
	public FieldInitValueSetMethodVisitor(int api, MethodVisitor mv,
		Map input, int fieldOpcode) {
		super(api, mv);
		this.initFieldMap = input;
		this.fieldOpcode = fieldOpcode;
	}
	@Override
	public void visitInsn(int opcode) {
		if (Opcodes.RETURN == opcode) {
		initWeaverField();
		}
		super.visitInsn(opcode);
	}
	private void initWeaverField() {
		if (initFieldMap == null) {
			return;
		}
		
		Iterator it = initFieldMap.keySet().iterator();
		while (it.hasNext()) {
			if (Opcodes.PUTFIELD == this.fieldOpcode) {
				visitVarInsn(Opcodes.ALOAD, 0);
			}
			Object key = it.next();
			
			FieldInfo fieldInfo = (FieldInfo) key;
			if ("visitLdcInsn".equals(fieldInfo.getVisitType())) {
				visitLdcInsn(initFieldMap.get(key));
			} else if ("visitInsn".equals(fieldInfo.getVisitType())) {
				visitInsn((Integer) initFieldMap.get(key));
			} else if ("visitIntInsn".equals(fieldInfo.getVisitType())) {
				visitIntInsn(Opcodes.BIPUSH, (Integer) initFieldMap.get(key));
			}
			visitFieldInsn(fieldOpcode, getTargetClassReader()
										.getClassName(), fieldInfo.getName(),
			fieldInfo.getDesc());
		}
		
//		for (Entry entry : initFieldMap.entrySet()) {
//			if (Opcodes.PUTFIELD == this.fieldOpcode) {
//			visitVarInsn(Opcodes.ALOAD, 0);
//			}
//			FieldInfo fieldInfo = entry.getKey();
//			if ("visitLdcInsn".equals(fieldInfo.getVisitType())) {
//			visitLdcInsn(entry.getValue());
//			} else if ("visitInsn".equals(fieldInfo.getVisitType())) {
//			visitInsn((Integer) entry.getValue());
//			} else if ("visitIntInsn".equals(fieldInfo.getVisitType())) {
//			visitIntInsn(Opcodes.BIPUSH, (Integer) entry.getValue());
//			}
//			visitFieldInsn(fieldOpcode, getTargetClassReader()
//			.getClassName(), fieldInfo.getName(),
//			fieldInfo.getDesc());
//		}
	}
	}
	/**
	* 织入类访问（只保留字段，方法）
	*/
class WeaverClassVisitor extends ClassVisitor {
	public WeaverClassVisitor(int api, ClassVisitor cv) {
	super(api, cv);
	}
	// 织入类的类信息删除
	@Override
	public void visit(int version, int access, String name,
	String signature, String superName, String[] interfaces) {
	}
	// 织入类的字段保留
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
									String signature, Object value) {
		return super.visitField(access, name, desc, signature, value);
	}
	// 织入类的方法保留
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
									String signature, String[] exceptions) {
	// 织入类的构造函数、静态块删除
	if (INTERNAL_INIT_METHOD_NAME.equals(name)
			|| INTERNAL_STSTIC_INIT_METHOD_NAME.equals(name)) {
		return null;
	}
	// 修改织入类中覆盖了目标类方法的访问权限(被存根类放宽的访问权限)
	if (targetMethods.containsKey(name)) {
		access = (int) targetMethods.get(name);
	}
	MethodVisitor visitMethod = super.visitMethod(access, name, desc,
													signature, exceptions);
	// 织入类的方法处理
	return new MethodVisitor(Opcodes.ASM5, visitMethod) {
	String owner = getTargetClassReader().getClassName();
	@Override
	public void visitMethodInsn(int opcode, String owner,
								String name, String desc, boolean itf) {
		// 织入类新增方法调用
		if (!targetMethods.containsKey(name)
							&& weaverMethodNames.contains(name)) {
			owner = this.owner;
		}
		// 非新增方法调用
		else {
		// 方法体中包含super关键字的方法调用
			if (owner.equals(targetStubClass)
							&& Opcodes.INVOKESPECIAL == opcode) {
				opcode = Opcodes.INVOKEVIRTUAL;
				owner = this.owner;
				name = name + "0";
			}
		}
		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}
	// 字段的owner为super的改为目标类
	@Override
	public void visitFieldInsn(int opcode, String owner,
								String name, String desc) {
		if (owner.equals(getWeaverClassReader().getClassName())) {
		owner = getTargetClassReader().getClassName();
		}
		super.visitFieldInsn(opcode, owner, name, desc);
	}
	};
	}
}
}