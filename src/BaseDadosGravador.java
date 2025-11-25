public class BaseDadosGravador {
    private boolean isRecording;
    private boolean isReproducing;
    private String pathFicheiro;
    
    public BaseDadosGravador() {
        this.isRecording = false;
    }
    
    public void setIsRecording(boolean value) {
    	this.isRecording = value;
    }
    
    public boolean getIsRecording() {
    	return isRecording;
    }
    
    public void setPathFicheiro(String value) {
    	this.pathFicheiro = value;
    }
    
    public void setIsReproducing(boolean value) {
    	this.isReproducing = value;
    }
    
    public boolean getIsReproducing() {
    	return isReproducing;
    }
    
    
    public String getPathFicheiro() {
    	return pathFicheiro;
    }

}
