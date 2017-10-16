package circles;


import java.util.stream.Stream;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Spinner;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * @author Jacob Beck
 */
public class Circles extends Application {
    
    public static final int ROWS = 4;
    public static final int COLS = 5;
    public static final int CELL_SIZE = 100;
    
    @Override
    public void start(Stage primaryStage) {
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        canvas = new Pane();
        starter = new Button("Circles");
        controls = new HBox(10.0);
        controls.setAlignment(Pos.CENTER);
        rowSpinner = new Spinner(1,5,5);
        colSpinner = new Spinner(1,5,5);
        cellsize = new Slider(50,150,50);
        xfactor = new Spinner(-3,3,0);
        yfactor = new Spinner(-3,3,0);
        rowSpinner.setPrefWidth(60.0);
        colSpinner.setPrefWidth(60.0);
        xfactor.setPrefWidth(60.0);
        yfactor.setPrefWidth(60.0);
       rowSpinner.valueProperty().addListener( e -> {launchCircles();});
       colSpinner.valueProperty().addListener(e -> {launchCircles();});
       xfactor.valueProperty().addListener(e -> {launchCircles();});
       yfactor.valueProperty().addListener(e -> {launchCircles();});
       cellsize = new Slider (50.0,150.0,100.0);
       cellsize.valueProperty().addListener(e -> { launchCircles();});
       
       Label cellLabel = new Label();
       cellLabel.setPrefWidth(30.0);
         cellLabel.textProperty().bind((ObservableValue)Bindings.createStringBinding(() -> String.format("%3d", (int)cellsize.getValue()), (Observable[])new Observable[]{cellsize.valueProperty()}));        
       controls.getChildren().addAll((Node[])(Object[])new Node[]{makeLabeledNode("Rows", (Node)rowSpinner), makeLabeledNode("Columns", (Node)colSpinner),makeLabeledNode("Cell Size", (Node)new HBox(new Node[]{cellsize, cellLabel})), makeLabeledNode("X Scale", (Node)xfactor),makeLabeledNode("Y Scale", (Node)yfactor)});


        canvas.setPrefSize(5 * 150 , 5 * 150);
        root.getChildren().addAll((Node[])(Object[]) new Node[]{canvas, controls});
        addButtonHandler();  // You must write
        primaryStage.setTitle("JavaFX Lab Exercise");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
       launchCircles();
    }
    
    private VBox makeLabeledNode(String label, Node node){
        VBox vb = new VBox(10.0, new Node[]{new Label(label), node});
        vb.setAlignment(Pos.CENTER);
        return vb;
    }
    
    private void launchCircles() {
        canvas.getChildren().clear();
        addAllRowsToCanvas(makeAllRows());
    }
    
    /**
     * This method adds the handler to the button that gives
     * this application its behavior.
     */
    private void addButtonHandler() {
      starter.setOnAction( e-> { launchCircles();});
    }
   
 
    private VBox root;
    private HBox controls; 
    private Pane canvas;
    private Button starter;
    private Spinner rowSpinner;
    private Spinner colSpinner;
    private Spinner xfactor;
    private Spinner yfactor;
    private Slider cellsize; 
    private int row;
    private int col;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Circles.launch((String[]) args);
    }
    
    private void addToCanvas(Circle circle){
        double toX , toY, fromX, fromY;
      toX = (double) col * cellsize.getValue() + cellsize.getValue()/2.0;
      toY = (double) row * cellsize.getValue() + cellsize.getValue()/2.0;
      fromX =  (double)((Integer)colSpinner.getValue()).intValue() * cellsize.getValue() - cellsize.getValue() / 2.0;
      fromY = (double)((Integer)rowSpinner.getValue()).intValue() * cellsize.getValue() - cellsize.getValue() / 2.0;
      circle.setFill((Paint)new Color(Math.random(), Math.random(), Math.random(), 1.0));
      circle.setCenterX(fromX);
      circle.setCenterY(fromY);
      canvas.getChildren().add(circle);
    TranslateTransition tt = new TranslateTransition(Duration.millis((double)500.0));
        tt.setNode((Node) circle);
        tt.setByX(toX - fromX);
        tt.setByY(toY - fromY);
        tt.play();
        ScaleTransition st = new ScaleTransition(Duration.millis((double)(500.0 * Math.random() + 500.0)));
        st.setNode((Node) circle);
        st.setByX((double)((Integer)xfactor.getValue()).intValue());
        st.setByY((double)((Integer)yfactor.getValue()).intValue());
        st.setCycleCount(-1);
        st.setAutoReverse(true);
        st.play();
       
    }
    
    public Stream<Circle> makeRow(){
    return Stream.generate(() -> new Circle(cellsize.getValue()/4)).limit((int) colSpinner.getValue()); 
    }
    
    public void addRowToCanvas(Stream<Circle> circle){
        col = 0;
        circle.forEach(c -> {addToCanvas(c); col++;}); 
    }
    
    public Stream<Stream<Circle>> makeAllRows(){
        return Stream.generate(() -> makeRow()).limit((int) rowSpinner.getValue());
    }
    
    public void addAllRowsToCanvas(Stream<Stream<Circle>> circle){
        row = 0;
        circle.forEach(r -> {addRowToCanvas(r); row++;});
    }
    
    
            
}