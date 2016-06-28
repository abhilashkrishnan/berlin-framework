package org.brelin.web.test;

import org.brelin.beans.annotation.AutoWired;
import org.brelin.beans.annotation.Bean;

/**
 * Created by ACER on 27-06-2016.
 */
@Bean(name = "Second")
public class SecondBean {
	private MyFirstBean myFirstBean;
	
	@AutoWired
	public void setMyFirstBean(MyFirstBean myFirstBean) {
		this.myFirstBean = myFirstBean;
	}
	
	public MyFirstBean getMyFirstBean() {
		return this.myFirstBean;
	}
}
