package tj.tool.usefultool;

public class Strings {

	/**
	 * 重复复制字符串
	 * @param string
	 * @param count
	 * @return
	 */
	public static String repeat(String string,int count)
	{
		if (string == null)
		{
			throw new NullPointerException();
		}
		if(count <= 1)
		{
			return (count == 0) ? "" : string;
		}
		
		final int len = string.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if(size != longSize)
		{
			throw new ArrayIndexOutOfBoundsException(
					new StringBuilder().append("Required array size too large: ").append(longSize).toString());
		}
		
		char[] array = new char[size];
		string.getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<=1) {
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}
	
	/**
	 * 字符串头部填充字符
	 * @param string
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padStart(String string, int minLength, char padChar) {
		if (string == null)
		{
			throw new NullPointerException();
		}
		if(string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		sb.append(string);
		return sb.toString();
	}
	
	/**
	 * 字符串结尾填充字符
	 * @param string
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padEnd(String string, int minLength, char padChar) {
		if (string == null)
		{
			throw new NullPointerException();
		}
		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		sb.append(string);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}
	
	/**
	 * 从头部寻找匹配的上的字符串
	 * @param a
	 * @param b
	 * @return
	 */
	public static String commonPrefix(CharSequence a, CharSequence b) {
		if (a == null || b == null) 
		{
			throw new NullPointerException();
		}
		
		int maxPrefixLength = Math.min(a.length(), b.length());
		int p = 0;
		while (p < maxPrefixLength && a.charAt(p) == b.charAt(p)) {
			p++;
		}
		if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)){
			p--;
		}
		return a.subSequence(0, p).toString();
	}
	
	/**
	 * 从尾部寻找匹配的上的字符串
	 * @param a
	 * @param b
	 * @return
	 */
	public static String commonSuffix(CharSequence a,CharSequence b) {
		if (a == null || b == null) 
		{
			throw new NullPointerException();
		}
		int maxSuffixLength = Math.min(a.length(), b.length());
		int s = 0;
		while (s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s -1)) {
			s++;
		}
		if(validSurrogatePairAt(a, a.length() - s - 1) || validSurrogatePairAt(b, b.length() - s - 1)) {
			s--;
		}
		return a.subSequence(a.length() - s, a.length()).toString();
	}
	
	static boolean validSurrogatePairAt(CharSequence string, int index)
	{
		return ((index >= 0) 
				&& (index <= string.length() - 2)
				&& (Character.isHighSurrogate(string.charAt(index)))
				&& (Character.isLowSurrogate(string.charAt(index + 1))));
	}
}
