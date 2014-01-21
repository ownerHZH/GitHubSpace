package test;
public class TestOr {
    public static void main(String[] args) {
		if(method1()||method2()||method3())
		{
			System.out.println("链接成功");
		}
	}
    
    public static boolean method1()
    {
    	System.out.println("method1");
    	return false;
    }
    public static  boolean method2()
    {
    	System.out.println("method2");
    	return true;
    }
    public static  boolean method3()
    {
    	System.out.println("method3");
    	return true;
    }
}
