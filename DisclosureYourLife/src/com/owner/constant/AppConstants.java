package com.owner.constant;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.owner.domain.Consume;
import com.owner.domain.ConsumeComment;
import com.owner.domain.Embarrass;
import com.owner.domain.EmbarrassComment;
import com.owner.domain.Income;
import com.owner.domain.IncomeComment;
import com.owner.domain.Picture;
import com.owner.domain.PlainLook;
import com.owner.domain.Spinner;
import com.owner.domain.User;
import com.owner.tools.GsonUtil;

public class AppConstants {
	public static int REQUEST_TIMEOUT = 20;
	public static int SO_TIMEOUT = 120;
	
	//用户ID，为了上传的假数据
	public static int USER_ID=1;
	public static User USER;
	
	//服务器所在链接
	public static String HttpHostAdress="http://192.168.0.102:8080/DisclosureYourLife/";
	
	public static String TARGET_NOT_FOUND_EXCEPTION = "Target host must not be null";
	public static String HTTPHOST_CONNECT_EXCEPTION = "HttpHostConnectException";
	
	public static Gson gson = GsonUtil.getGson();
	
	//获取consume列表action
	public static String RequestAction=HttpHostAdress+"ConsumeList.action";
	//获取income列表action
	public static String IncomeRequestAction=HttpHostAdress+"IncomeList.action";
	//获取embarrass列表action
	public static String EmbarrassRequestAction=HttpHostAdress+"EmbarrassList.action";
	//加载更多的图片action
	public static String LoadMorePicturesAction=HttpHostAdress+"LoadMorePictures.action";
	//embarrass上传action
	public static String RequestUpLoadAction=HttpHostAdress+"Upload.action"; 
	//consume上传action
	public static String ConsumeUpLoadAction=HttpHostAdress+"ConsumeUpload.action"; 	
	//income上传action
	public static String IncomeUpLoadAction=HttpHostAdress+"IncomeUpload.action";
	
	//embarrass评论上传action
	public static String EmbarrassCommentUpLoadAction=HttpHostAdress+"EmbarrassCommentCommit.action"; 
	//consume评论上传action
	public static String ConsumeCommentUpLoadAction=HttpHostAdress+"ConsumeCommentCommit.action"; 	
	//income评论上传action
	public static String IncomeCommentUpLoadAction=HttpHostAdress+"IncomeCommentCommit.action";
	
	//获取consume列表action
	public static String ConsumeCommentRequestAction=HttpHostAdress+"ConsumeCommentList.action";
	//获取income列表action
	public static String IncomeCommentRequestAction=HttpHostAdress+"IncomeCommentList.action";
	//获取embarrass列表action
	public static String EmbarrassCommentRequestAction=HttpHostAdress+"EmbarrassCommentList.action";
	
	//获取plainLook列表action
    public static String PlainLookRequestAction=HttpHostAdress+"QueryAllPlainLook.action";
	//上传plainLook列表action
	public static String PlainLookUpLoadAction=HttpHostAdress+"UploadPlainLook.action";
	//点赞更新数据库
	public static String UpdatePlainLookCount=HttpHostAdress+"UpdatePlainLookCount.action";
	//添加用户action
	public static String AddUserAction=HttpHostAdress+"AddUser.action";
	//获取Spinner数据的action
	public static String SpinnerAction=HttpHostAdress+"GetSpinner.action";
	
	public static Type type_consumeList = new TypeToken<List<Consume>>() {
	}.getType();
	public static Type type_incomeList = new TypeToken<List<Income>>() {
	}.getType();
	public static Type type_embarrassList = new TypeToken<List<Embarrass>>() {
	}.getType();
	public static Type type_pictureList = new TypeToken<List<Picture>>() {
	}.getType();
	
	public static Type type_consumeCommentList = new TypeToken<List<ConsumeComment>>() {
	}.getType();
	public static Type type_incomeCommentList = new TypeToken<List<IncomeComment>>() {
	}.getType();
	public static Type type_embarrassCommentList = new TypeToken<List<EmbarrassComment>>() {
	}.getType();
	public static Type type_plainLookList = new TypeToken<List<PlainLook>>() {
	}.getType();
	public static Type type_user = new TypeToken<User>() {
	}.getType();
	public static Type type_spinnerList= new TypeToken<List<Spinner>>() {
	}.getType();

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String IMAGES = "com.owner.constant.IMAGES";
		public static final String IMAGE_POSITION = "com.owner.constant.IMAGE_POSITION";
		public static final String IDS = "com.owner.constant.IMAGE_IDS";
		public static final String COUNTS = "com.owner.constant.IMAGE_COUNTS";
		public static final String BCOUNTS = "com.owner.constant.IMAGE_BCOUNTS";
	}
	
	private AppConstants() {
	}
}
