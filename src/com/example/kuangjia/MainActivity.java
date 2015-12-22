package com.example.kuangjia;

import android.os.Bundle;
import android.view.Menu;
import java.sql.SQLException;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import android.content.Intent;

import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	
	private EditText stuNO;
	private EditText stuName;
	private EditText stuAge;
	private EditText stuSex;
	private EditText stuScore;
	private EditText stuAddress;
	
	private Student mStudent;
	private Dao<Student,Integer> stuDao;
	
	private final int MENU_ADD = Menu.FIRST;
	private final int MENU_VIEWALL = Menu.FIRST+1;
	private final int MENU_EDIT = Menu.FIRST+2;
	
	private Bundle mBundle = new Bundle();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
    }
    
    /**
     * 初始化UI界面
     */
    private void initializeViews(){
    	stuNO = (EditText)findViewById(R.id.stuno);
    	stuName = (EditText)findViewById(R.id.name);
    	stuAge = (EditText)findViewById(R.id.age);
    	stuSex = (EditText)findViewById(R.id.sex);
    	stuScore = (EditText)findViewById(R.id.score);
    	stuAddress = (EditText)findViewById(R.id.address);
    	
    	mBundle = getIntent().getExtras();
    	if(mBundle!=null && mBundle.getString("action").equals("viewone")){
    		mStudent = (Student)getIntent().getSerializableExtra("entity");
    		setStudentUIData(mStudent);
    	}
    	
    	if(mBundle!=null && mBundle.getString("action").equals("edit")){
    		mStudent = (Student)getIntent().getSerializableExtra("entity");
    		setStudentUIData(mStudent);
    	}
    }
    
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	if(mBundle!=null && mBundle.getString("action").equals("viewone"))
    		return false;
    	else
    		return super.onPrepareOptionsMenu(menu);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(mBundle!=null && mBundle.getString("action").equals("edit")){
			menu.add(1,MENU_EDIT,0,"保存");
		}else{
	    	menu.add(0,MENU_ADD,0,"增加");
	    	menu.add(0,MENU_VIEWALL,0,"查看");
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD://添加
			try {
				stuDao = getHelper().getStudentDao();
				getStudentData();//获取界面的值
				if(mStudent != null){
					//创建记录项【添加】
					stuDao.create(mStudent);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case MENU_VIEWALL://查看数据库里面的全部的数据
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, StudentListActivity.class);
			startActivity(intent);
			break;
		case MENU_EDIT:
			try {
				getStudentData();//获取界面的值
				stuDao = getHelper().getStudentDao();
				if(mStudent != null){
					//更新某记录项
					stuDao.update(mStudent);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
     * 获取界面值(实体信息)
     */
    private void getStudentData(){
    	mStudent = new Student();
    	mStudent.setStuNO(stuNO.getText().toString());
    	mStudent.setName(stuName.getText().toString());
    	mStudent.setAge(Integer.parseInt(stuAge.getText().toString()));
    	mStudent.setSex(stuSex.getText().toString());
    	mStudent.setScore(Double.parseDouble(stuScore.getText().toString()));
    	mStudent.setAddress(stuAddress.getText().toString());
    }
    
    /**
     * 赋值给UI界面
     * @param student
     */
    private void setStudentUIData(Student student){
    	stuNO.setText(student.getStuNO());
    	stuName.setText(student.getName());
    	stuAge.setText(String.valueOf(student.getAge()));
    	stuSex.setText(student.getSex());
    	stuScore.setText(String.valueOf(student.getScore()));
    	stuAddress.setText(student.getAddress());
    }
}