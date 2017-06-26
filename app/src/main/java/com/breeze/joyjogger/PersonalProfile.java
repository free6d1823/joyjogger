package com.breeze.joyjogger;

public class PersonalProfile {
 
		public int gender;//0=femail, 1=mail
		public float height; //cm
		public float weight; //kg
		public float waist; //cm
		float bmi;
		public PersonalProfile(int g, float h, float wt, float ws){
			gender = g;
			height = h;
			weight = wt;
			waist = ws;
			 bmi = weight *10000/(height*height);
		}
 
		public float getBmi(){
			return bmi;
		}
		
		public int getBmiServilityColorId(){
			if(bmi < 18.5)
				return R.color.color_warning; //BMI_THIN;
			if(bmi < 24)
				return R.color.color_normal;
			if(bmi < 30)
				return R.color.color_warning;
			else
				return R.color.color_dangerous;
		}
		public int getBmiDescriptionId()
		{
			if(bmi < 18.5)
				return R.string.str_bmi_thin; //BMI_THIN;
			if(bmi < 24)
				return R.string.str_bmi_normal;
			if(bmi < 27)
				return R.string.str_bmi_heavy;
			if(bmi < 30)
				return R.string.str_bmi_fat;
			if(bmi < 35)
				return R.string.str_bmi_middle_fat;	
			else
				return R.string.str_bmi_too_fat;

		}
		public int getWaistDescId(){
			if(gender == 0){
				if(waist >= 80)
					return R.string.str_waist_fat;
				if(waist <= 55)
					return R.string.str_waist_thin;
			}
			else{
				if(waist >= 90)
					return R.string.str_waist_fat;	
				if(waist <= 60)
					return R.string.str_waist_thin;			
			}
			return R.string.str_waist_normal;
		} 
		public int getWaistServilityColorId(){
			if(gender == 0){
				if(waist >= 80)
					return R.color.color_dangerous;
				if(waist <= 55)
					return R.color.color_warning; //too thin
			}
			else{ 
				if(waist >= 90)
					return R.color.color_dangerous;	
				if(waist <= 60)
					return R.color.color_warning; //too thin		
			}
			return R.color.color_normal;
 
		}	
}
