public class Record{
	
	private String gender;
	private int age;
	private int academicPressure;
	private int studySatisfaction;
	private String sleepDuration;
	private String dietaryHabits;
	private String suicidalThoughts;
	private int studyHours;
	private int financialStress;
	private boolean familyHistory;
	private int wellnessScore;

	public Record(){

	}

	public String getGender(){
		return gender;
	}
	public void setGender(String gender){
		this.gender = gender;
	}

	public int getAge(){
		return age;
	}
	public void setAge(int age){
		this.age = age;
	}

	public int getAcademicPressure(){
		return academicPressure;
	}
	public void setAcademicPressure(int academicPressure){
		this.academicPressure = academicPressure;
	}

	public int getStudySatisfaction(){ 
		return studySatisfaction; 
	}
    public void setStudySatisfaction(int studySatisfaction){ 
    	this.studySatisfaction = studySatisfaction; 
    }

    public String getSleepDuration(){ 
    	return sleepDuration; 
    }
    public void setSleepDuration(String sleepDuration){ 
    	this.sleepDuration = sleepDuration; 
    }

    public String getDietaryHabits(){ 
    	return dietaryHabits; 
    }

    public void setDietaryHabits(String dietaryHabits){ 
    	this.dietaryHabits = dietaryHabits; 
    }

    public String getSuicidalThoughts(){ 
    	return suicidalThoughts; 
    }
    public void setSuicidalThoughts(String suicidalThoughts){ 
    	this.suicidalThoughts = suicidalThoughts; 
    }

    public int getStudyHours(){ 
    	return studyHours; 
    }
    public void setStudyHours(int studyHours){ 
    	this.studyHours = studyHours; 
    }

    public int getFinancialStress(){ 
    	return financialStress; 
    }
    public void setFinancialStress(int financialStress){ 
    	this.financialStress = financialStress; 
    }

    public boolean getFamilyHistory(){ 
    	return familyHistory; 
    }
    public void setFamilyHistory(boolean familyHistory){ 
    	this.familyHistory = familyHistory; 
    }

    public int getWellnessScore(){ 
    	return wellnessScore; 
    }
    public void setWellnessScore(int wellnessScore){ 
    	this.wellnessScore = wellnessScore; 
    }
}

