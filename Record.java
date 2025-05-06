public class Record{
	
	private String gender;
	private int age;
	private int academicPressure;
	private int studySatisfaction;
	private String sleepDuration;
	private String dietaryHabits;
	private boolean suicidalThoughts;
	private int studyHours;
	private int financialStress;
	private boolean familyHistory;
	private int wellnessScore;

	public Record(){

	}

	public Record(String gender, int age, int academicPressure, int studySatisfaction, String sleepDuration, String dietaryHabits,
				  boolean suicidalThoughts, int studyHours, int financialStress, boolean familyHistory, int wellnessScore) {
		this.gender = gender;
		this.age = age;
		this.academicPressure = academicPressure;
		this.studySatisfaction = studySatisfaction;
		this.sleepDuration = sleepDuration;
		this.dietaryHabits = dietaryHabits;
		this.suicidalThoughts = suicidalThoughts;
		this.studyHours = studyHours;
		this.financialStress = financialStress;
		this.familyHistory = familyHistory;
		this.wellnessScore = wellnessScore;
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

    public boolean getSuicidalThoughts(){ 
    	return suicidalThoughts; 
    }
    public void setSuicidalThoughts(boolean suicidalThoughts){ 
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

	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass()) {
			return false;
		} else {
			Record other = (Record) obj;
			return (this.gender.equals(other.getGender()) && this.age == other.getAge() && this.academicPressure == other.getAcademicPressure()
				&& this.studySatisfaction == other.getStudySatisfaction() && this.sleepDuration.equals(other.getSleepDuration())
				&& this.dietaryHabits.equals(other.getDietaryHabits()) && this.suicidalThoughts == other.getSuicidalThoughts() && this.studyHours == other.getStudyHours()
				&& this.financialStress == other.getFinancialStress() && this.familyHistory == this.getFamilyHistory() && this.wellnessScore == other.getWellnessScore()) ? true : false;
		}
	}

	@Override
	public String toString() {
		return "[" + this.gender + "," + this.age + "," + this.academicPressure + "," + this.studySatisfaction + "," + this.sleepDuration + "," + this.dietaryHabits
			   + "," + this.suicidalThoughts + "," + this.studyHours + "," + this.financialStress + "," + this.familyHistory + "," + this.wellnessScore + "]";
	}
}

