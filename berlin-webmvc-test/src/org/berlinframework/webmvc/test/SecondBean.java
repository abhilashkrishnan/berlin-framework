package org.berlinframework.webmvc.test;

import org.berlinframework.context.annotation.AutoWired;
import org.berlinframework.context.annotation.Bean;
import org.berlinframework.context.annotation.Qualifier;

/**
 * @author Abhilash Krishnan
 */
@Bean(name = "Second")
public class SecondBean {
	
	@AutoWired
	@Qualifier(name = "first")
	private MyFirstBean myFirstBean;
	
	private ThirdBean thirdBean;
	
	public void setMyFirstBean(MyFirstBean myFirstBean) {
		this.myFirstBean = myFirstBean;
	}
	
	public MyFirstBean getMyFirstBean() {
		return this.myFirstBean;
	}
	
	@AutoWired
	public void setThirdBean(ThirdBean thirdBean) {
		this.thirdBean = thirdBean;
	}
	
	public ThirdBean getThirdBean() {
		return this.thirdBean;
	}
}
