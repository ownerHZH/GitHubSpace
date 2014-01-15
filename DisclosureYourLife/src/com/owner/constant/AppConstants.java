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
import com.owner.tools.GsonUtil;

public class AppConstants {
	public static int REQUEST_TIMEOUT = 20;
	public static int SO_TIMEOUT = 120;
	
	//用户ID，为了上传的假数据
	public static int USER_ID=1;  
	
	//服务器所在链接
	public static String HttpHostAdress="http://192.168.1.150:8080/DisclosureYourLife/";
	
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

}
