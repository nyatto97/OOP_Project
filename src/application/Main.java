package application;
	
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

	private ArrayList<Rectangle> rect;
	int selectedAlgo = 0;
	private BooleanBinding anyPlaying;
	private IntegerProperty nextTransitionIndex;
	private Button playButton;
	private Button startButton;
	private Button reverseButton;
	private int numOfNumbers = 12;
	private TextField textField;
	
	private SequentialTransition sq;
	
	private final String COLOR_TO = "41C4C0";
	private final String COLOR_BACK = "ADD8E6";
	private final String COLOR_CHANGED = "4175B3";
	private final String COLOR_MINMAX = "F4A460";
	
	private Text BubbleSort;
	private Text MergeSort;
	private Text SelectionSort;
	private Text BucketSort;
	
	public BooleanBinding getAnyPlaying() {
		return anyPlaying;
	}

	public void setAnyPlaying(BooleanBinding anyPlaying) {
		this.anyPlaying = anyPlaying;
	}
	
	private ArrayList<Animation> transitions = new ArrayList<Animation>();
	private ArrayList<Animation> backTransitions = new ArrayList<Animation>();
	
	private ParallelTransition swapSelect(StackPane l1, StackPane l2, ArrayList<StackPane> list, double speed, SequentialTransition sq, ArrayList<Animation> back) {
        int num = 1;
        StackPane  fSp = null;
        
        boolean outerBreak = false;
        for (int i = 0; i < list.size(); i++) {
            if (outerBreak) break;
            if (list.get(i) == l1 || list.get(i) == l2) {
                fSp = list.get(i);
                for (int j = list.indexOf(fSp) + 1; j < list.size(); j++) {
                    if ((list.get(j) == l1 && list.get(j) != fSp) || (list.get(j) == l2 && list.get(j) != fSp)) {
                        outerBreak = true;
                        num = j - i;
                        break;
                    }
                }
            }
        }
        
        TranslateTransition t1 = new TranslateTransition();
        TranslateTransition t2 = new TranslateTransition();
        ParallelTransition pl = new ParallelTransition();
        num *= 60; 
        t1.setNode(l1);
        t1.setByX(num);
        t1.setDuration(Duration.millis(speed));
        t2.setNode(l2);
        t2.setDuration(Duration.millis(speed)); 
        t2.setByX(-num);
        pl.getChildren().addAll(t1, t2);
        
        Collections.swap(list, list.indexOf(l1), list.indexOf(l2));
        Collections.swap(rect, list.indexOf(l1), list.indexOf(l2));
        
        TranslateTransition t3 = new TranslateTransition();
        t3.setNode(l1);
        t3.setDuration(Duration.millis(speed));
        t3.setByX(-num);
        TranslateTransition t4 = new TranslateTransition();
        t4.setNode(l2);
        t4.setByX(num);
        t4.setDuration(Duration.millis(speed));      
        ParallelTransition pl1 = new ParallelTransition();
        pl1.getChildren().addAll(t3, t4);
        back.add(pl1);
        
        TranslateTransition t5 = new TranslateTransition(Duration.millis(speed),l1);
        t5.setByX(num);
        TranslateTransition t6 = new TranslateTransition(Duration.millis(speed),l2);
        t6.setByX(-num);
        ParallelTransition pl2 = new ParallelTransition();  
        pl2.getChildren().addAll(t5, t6);
        sq.getChildren().add(pl2);
        
        return pl;
    }
	
	private void moveUp(StackPane l1,int num, ArrayList<StackPane> list, ArrayList<Animation> ani, ArrayList<Animation> back,
			SequentialTransition sq) {
		TranslateTransition t1 = new TranslateTransition();
		t1.setNode(l1);
		t1.setDuration(Duration.millis(400));	
		num *= 60;
		t1.setByY(-200);
		t1.setByX(num);
		ParallelTransition pr = new ParallelTransition();
		pr.getChildren().addAll(t1, changeColor(Color.valueOf(COLOR_TO), rect.get(list.indexOf(l1))));
		ani.add(pr);
		
		TranslateTransition t3 = new TranslateTransition();
		t3.setNode(l1);
		t3.setDuration(Duration.millis(400));
		t3.setByY(-200);
		t3.setByX(num);
		ParallelTransition pr3 = new ParallelTransition();
		pr3.getChildren().addAll(t3, changeColor(Color.valueOf(COLOR_TO), rect.get(list.indexOf(l1))));
		sq.getChildren().add(pr3);
		
		TranslateTransition t2 = new TranslateTransition();
		t2.setNode(l1);
		t2.setDuration(Duration.millis(400));	
		t2.setByY(200);
		t2.setByX(-num);
		ParallelTransition pr2 = new ParallelTransition();
		pr2.getChildren().addAll(t2, changeColor(Color.valueOf(COLOR_BACK), rect.get(list.indexOf(l1))));
		back.add(pr2);
	}
	
	private void moveDown(StackPane l1,int num,int y, ArrayList<StackPane> list, ArrayList<Animation> ani, ArrayList<Animation> back, 
			SequentialTransition sq) {
		TranslateTransition t1 = new TranslateTransition();
		t1.setNode(l1);
		t1.setDuration(Duration.millis(400));
		t1.setByY(y);
		t1.setByX(num * 60);
		ParallelTransition pr = new ParallelTransition();
		pr.getChildren().addAll(t1, changeColor(Color.valueOf(COLOR_BACK), rect.get(list.indexOf(l1))));
		ani.add(pr);
		
		TranslateTransition t3 = new TranslateTransition();
		t3.setNode(l1);
		t3.setDuration(Duration.millis(400));
		t3.setByY(y);
		t3.setByX(num * 60);
		ParallelTransition pr3 = new ParallelTransition();
		pr3.getChildren().addAll(t3, changeColor(Color.valueOf(COLOR_BACK), rect.get(list.indexOf(l1))));
		sq.getChildren().add(pr3);
		
		TranslateTransition t2 = new TranslateTransition();
		t2.setNode(l1);
		t2.setDuration(Duration.millis(400));	
		t2.setByY(-y);
		t2.setByX(-60 * num);
		ParallelTransition pr2 = new ParallelTransition();
		pr2.getChildren().addAll(t2, changeColor(Color.valueOf(COLOR_TO), rect.get(list.indexOf(l1))));
		back.add(pr2);
	}
	
	private FillTransition changeColor(Color color,Rectangle rectangle) {
		FillTransition fill = new FillTransition(new Duration(200), rectangle);
		fill.setToValue(color);
		return fill;
	}

	private void merge(int arr[], int l, int m, int r, ArrayList<StackPane> list, ArrayList<Animation> ani, ArrayList<Animation> back, SequentialTransition sq){
		int n1 = m - l + 1; 
        int n2 = r - m; 
        int L[] = new int [n1]; 
        int R[] = new int [n2]; 
        ArrayList<StackPane> listClone = new ArrayList<StackPane>();
        ArrayList<Rectangle> rectClone = new ArrayList<Rectangle>();
        
        int arrClone[] = arr.clone();
        boolean arrBoolean[] = new boolean[r-l+1];
        
        for (int i=0; i<n1; ++i) 
            L[i] = arr[l + i]; 
        for (int j=0; j<n2; ++j) 
            R[j] = arr[m + 1+ j];
        
        ParallelTransition pr = new ParallelTransition();
        ParallelTransition pr1 = new ParallelTransition();
        ParallelTransition pr2 = new ParallelTransition();
        
        for(int i1=l; i1<=r; i1++) {
        	pr.getChildren().add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(i1)));
        	pr2.getChildren().add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(i1)));
        	pr1.getChildren().add(changeColor(Color.valueOf(COLOR_BACK), rect.get(i1)));
        }
        ani.add(pr);
        back.add(pr1);
        sq.getChildren().add(pr2);
        
        int i = 0, j = 0; 
             
        int k = l; 
        while (i < n1 && j < n2) 
        { 
            if (L[i] <= R[j]) 
            {       
                arr[k] = L[i];
                moveUp(list.get(i+l),k - (i+l), list, ani, back, sq);
                i++; 
            } 
            else 
            { 
                arr[k] = R[j];  
                moveUp(list.get(j+m+1),k - (j + m +1), list, ani,back, sq);
                j++; 
            } 
            arrBoolean[k-l] = true;
            k++; 
        } 
        
        while (i < n1) 
        { 
            arr[k] = L[i];
            moveUp(list.get(i+l), k - (i+l), list, ani, back, sq);
            i++; 
            arrBoolean[k-l] = true;
            k++; 
        } 
        
        while (j < n2) 
        { 
            arr[k] = R[j]; 
            moveUp(list.get(j+m+1), k - (j + m +1), list, ani, back, sq);
            j++; 
            arrBoolean[k-l] = true;
            k++; 
        } 
        
        for(int i1=l; i1<=r; i1++) {
        	for(int i2=l;i2<=r;i2++) {
        		if (arr[i1] == arrClone[i2] && arrBoolean[i2-l] == true) {
        			listClone.add(list.get(i2));
        			rectClone.add(rect.get(i2));
        			arrBoolean[i2-l] = false;
        			break;
        		}
        	}
        }
        
        for(int i1=l; i1<=r; i1++) {
        	list.remove(i1);
        	list.add(i1, listClone.get(i1-l));   
        	
        	rect.remove(i1);
        	rect.add(i1, rectClone.get(i1-l));   
        	
        }  
        
        for (int i1=l; i1<=r; i1++)
        	moveDown(list.get(i1),0,200, list, ani, back, sq);
        
	}
	
	
	private void MergeSort(int arr[], int l, int r,  ArrayList<StackPane> list, ArrayList<Animation> ani, ArrayList<Animation> back, SequentialTransition sq) 
    { 
        if (l < r) 
        { 
        	
            // Tìm phần tử ở giữa 
            int m = (l+r)/2; 
            // Gọi đệ quy hàm mergesort với 2 nửa 
            MergeSort(arr, l, m, list, ani, back, sq); 
            MergeSort(arr , m+1, r, list, ani, back, sq); 
  
            // Gộp 2 nửa đã được sắp xếp 
            merge(arr, l, m, r, list, ani, back, sq); 
        } 
    } 
	
	
	private ArrayList<Animation> BubbleSort(int arr[], ArrayList<StackPane> list, SequentialTransition sq, ArrayList<Animation> back) {
		ArrayList<Animation> ani = new ArrayList<Animation>();
        int temp;
        for (int i = 0; i < arr.length - 1; i++) {  	
        	sq.getChildren().add(changeColor(Color.valueOf(COLOR_TO), rect.get(0)));
        	ani.add(changeColor(Color.valueOf(COLOR_TO), rect.get(0)));
        	back.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(0)));
            for (int j = 1; j < arr.length - i; j++) {
            	sq.getChildren().add(changeColor(Color.valueOf(COLOR_TO), rect.get(j)));
            	ani.add(changeColor(Color.valueOf(COLOR_TO), rect.get(j)));
            	back.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(j)));
                if (arr[j - 1] < arr[j]) {
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                    ani.add(swapSelect(list.get(j-1), list.get(j), list, 400, sq, backTransitions));                  
                }
                sq.getChildren().add(changeColor(Color.valueOf(COLOR_BACK), rect.get(j-1)));  
                ani.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(j-1)));
                back.add(changeColor(Color.valueOf(COLOR_TO), rect.get(j-1)));
            }   
            sq.getChildren().add(changeColor(Color.valueOf(COLOR_MINMAX), rect.get(arr.length-i-1)));
            ani.add(changeColor(Color.valueOf(COLOR_MINMAX), rect.get(arr.length-i-1)));
            back.add(changeColor(Color.valueOf(COLOR_TO), rect.get(arr.length-i-1)));
        }
        ParallelTransition pr = new ParallelTransition();
        ParallelTransition pr1 = new ParallelTransition();
        for (int i = 0; i < arr.length; i++) {
        	pr.getChildren().add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(i)));
        	pr1.getChildren().add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(i)));
        }
        ani.add(pr);
        sq.getChildren().add(pr);
        back.add(pr1);
        return ani;
    }
	
	private void setTextOff(Text text, int algorithm) {
		text.setStyle("-fx-underline:false; \n"+"-fx-font: 20px Tahoma;\n" +
                "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
                "    -fx-stroke: grey;\n" +
                "    -fx-stroke-width: 1;");
		algorithm = -1;
	}
	
	private ArrayList<Animation> SelectionSort(int arr[], ArrayList<StackPane> list, SequentialTransition sq, ArrayList<Animation> back) {
		ArrayList<Animation> ani = new ArrayList<Animation>();
        int i, j, minIndex, tmp;
        int n = arr.length;
        for (i = 0; i < n - 1; i++) {
        	Color tmp1 = (Color) rect.get(i).getFill();
        	ani.add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(i)));
        	sq.getChildren().add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(i)));
        	back.add(changeColor(tmp1, rect.get(i)));
        	
        	
            minIndex = i;
            for (j = i + 1; j < n; j++) {
            	ani.add(changeColor(Color.valueOf(COLOR_TO), rect.get(j)));
            	back.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(j)));
            	sq.getChildren().add(changeColor(Color.valueOf(COLOR_TO), rect.get(j)));
            	if (arr[j] < arr[minIndex]) {
            		if(minIndex != i) {
            			ani.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(minIndex)));
            			sq.getChildren().add(changeColor(Color.valueOf(COLOR_BACK), rect.get(minIndex)));
            			back.add(changeColor(Color.valueOf(COLOR_MINMAX), rect.get(minIndex)));
            		}			
            		else {
            			ani.add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(minIndex)));
            			sq.getChildren().add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(minIndex)));
            			back.add(changeColor(Color.valueOf(COLOR_TO), rect.get(j)));
            		}
            		
            		
            		minIndex = j;
            		if(minIndex != i) {
            			ani.add(changeColor(Color.valueOf(COLOR_MINMAX), rect.get(minIndex)));
            			sq.getChildren().add(changeColor(Color.valueOf(COLOR_MINMAX), rect.get(minIndex)));
            			back.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(minIndex)));
            		}
            			
            		else {
            			back.add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(minIndex)));
            			sq.getChildren().add(changeColor(Color.valueOf(COLOR_TO), rect.get(minIndex)));
            			ani.add(changeColor(Color.valueOf(COLOR_TO), rect.get(minIndex)));
            		}      		
            	}
            	else {
            		ani.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(j)));
            		sq.getChildren().add(changeColor(Color.valueOf(COLOR_BACK), rect.get(j)));
            		back.add(changeColor(Color.valueOf(COLOR_TO), rect.get(j)));
            	}            	
            }
                    
            if (minIndex != i) {
                tmp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = tmp;
                ani.add(swapSelect(list.get(i), list.get(minIndex), list, 400, sq, backTransitions));
                
                ani.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(minIndex)));
                sq.getChildren().add(changeColor(Color.valueOf(COLOR_BACK), rect.get(minIndex)));
                back.add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(minIndex)));
            }else {
            	ani.add(changeColor(Color.valueOf(COLOR_MINMAX), rect.get(minIndex)));
                sq.getChildren().add(changeColor(Color.valueOf(COLOR_MINMAX), rect.get(minIndex)));
                back.add(changeColor(Color.valueOf(COLOR_CHANGED), rect.get(minIndex)));
            }
        }
        ParallelTransition pr = new ParallelTransition();
    	ParallelTransition pr1 = new ParallelTransition();
        for (i = 0; i < n ; i++) {
    		pr.getChildren().add(changeColor(Color.valueOf(COLOR_BACK), rect.get(i)));      		
    		pr1.getChildren().add(changeColor(Color.valueOf(COLOR_BACK), rect.get(i)));
    		
    	}
    	ani.add(pr);
    	sq.getChildren().add(pr1);
        return ani;
    }
	
	private int[] generateArray(List<StackPane> list) {
        int arr[] = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(list.get(i).getId());
        }
        return arr;
    }
	
	private void BucketSort(int[] arr, ArrayList<StackPane> list, ArrayList<Animation> ani, ArrayList<Animation> back, SequentialTransition sq){
		int i, j;
		int[] bucket = new int[arr.length];
		int[] arrClone = arr.clone();
		boolean arrBoolean[] = new boolean[list.size()];
		ArrayList<StackPane> stackClone = new ArrayList<StackPane>();
		ArrayList<Rectangle> rectClone = new ArrayList<Rectangle>();
		
		for(i=0;i < list.size(); i++) {
			moveUp(list.get(i), 0, list, ani, back, sq);
			arrBoolean[i] = true;
		}
		
		 int counter = 0;
	     LinkedList<LinkedList<Integer>> allBuckets = new LinkedList<LinkedList<Integer>>();

	     for(i = 0; i < 5; i++) {
	          allBuckets.addLast(new LinkedList<Integer>());
	     }

	     for (i = 0; i < arr.length; i++) {
	          j = arr[ i ] / 20;
	          allBuckets.get( j ).addLast( arr[ i ] );
	     }

	     for(i = 0; i < 5; i++){
	          for(j = 0; j < allBuckets.get( i ).size(); j++) {
	               arr[counter] = allBuckets.get( i ).get( j );
	               bucket[counter] = i;
	               counter++;	               
	          }
	     }
	     
	     for(int i1=0; i1<arr.length; i1++) {
	        	for(int i2=0;i2<arr.length;i2++) {
	        		if (arr[i1] == arrClone[i2] && arrBoolean[i2] == true) {
	        	        moveDown(list.get(i2),i1-i2,200, list, ani, back, sq);
	        			stackClone.add(list.get(i2));
	        			rectClone.add(rect.get(i2));
	        			arrBoolean[i2] = false;
	        			switch(bucket[i1]) {
	        				case 0:
	        					sq.getChildren().add(changeColor(Color.valueOf("E9967A"), rect.get(i2)));
	        					ani.add(changeColor(Color.valueOf("E9967A"), rect.get(i2)));
	        					
	        					break;
	        				case 1:
	        					sq.getChildren().add(changeColor(Color.valueOf("98FB98"), rect.get(i2)));
	        					ani.add(changeColor(Color.valueOf("98FB98"), rect.get(i2)));
	        					break;
	        				case 2:
	        					sq.getChildren().add(changeColor(Color.valueOf("F08080"), rect.get(i2)));
	        					ani.add(changeColor(Color.valueOf("F08080"), rect.get(i2)));
	        					break;
	        				case 3:
	        					sq.getChildren().add(changeColor(Color.valueOf("7FFFD4"), rect.get(i2)));
	        					ani.add(changeColor(Color.valueOf("7FFFD4"), rect.get(i2)));
	        					break;
	        				case 4:
	        					sq.getChildren().add(changeColor(Color.valueOf("F0E68C"), rect.get(i2)));
	        					ani.add(changeColor(Color.valueOf("F0E68C"), rect.get(i2)));
	        					break;
	        			}
	        			back.add(changeColor(Color.valueOf(COLOR_BACK), rect.get(i2)));
	        			break;
	        		}
	        	}
	        }
	        
	        for(int i1=0; i1<arr.length; i1++) {
	        	list.remove(i1);
	        	list.add(i1, stackClone.get(i1));   
	        	
	        	rect.remove(i1);
	        	rect.add(i1, rectClone.get(i1));   
	        	
	        }  
	     
	     insertionSort(arr, list, sq,ani, back);
	    
	}
	
	private void insertionSort(int[] arr, ArrayList<StackPane> list, SequentialTransition sq,ArrayList<Animation> ani, ArrayList<Animation> back) {
        int temp;
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                    ani.add(swapSelect(list.get(j-1), list.get(j), list, 400, sq, back));
                } else {
                    break;
                }
            }
        }
	}
	
	private HBox boxes;
	@Override
	public void start(Stage primaryStage) {
		try {
			rect = new ArrayList<Rectangle>();
			sq = new SequentialTransition();
			boxes = new HBox(20);
			boxes.setAlignment(Pos.CENTER);
			textField = new TextField("12");
			
			Button button = new Button("Get num of numbers");
			
			button.setOnAction(action -> {
	            numOfNumbers = Integer.parseInt(textField.getText());
	        });
			
			ArrayList<StackPane> stackPaneList = new ArrayList<StackPane>();
			Random random = new Random(15);
			DropShadow dropShadow2 = new DropShadow();
	        dropShadow2.setOffsetX(5.0);
	        dropShadow2.setOffsetY(5.0);
	        dropShadow2.setRadius(5);
	        dropShadow2.setColor(Color.valueOf("#808080"));
			for (int i = 0; i < numOfNumbers; i++) {
				int num = random.nextInt(100);
				Rectangle rectangle = new Rectangle(40, (num * 10)*0.1 + 50);
	            rectangle.setArcHeight(20);
	            rectangle.setArcWidth(20);
	            rectangle.setStroke(Color.BLACK);
	            rectangle.setFill(Color.valueOf("#ADD8E6"));
	            rectangle.setEffect(dropShadow2);
	            rect.add(rectangle);
	            
	            Text text = new Text(String.valueOf(num));
	            StackPane stackPane = new StackPane();
	            stackPane.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
	            stackPane.setId(String.valueOf(num));
	            stackPane.getChildren().addAll(rectangle, text);
	            stackPaneList.add(stackPane);
	        }
			
			boxes.getChildren().addAll(stackPaneList);
			nextTransitionIndex = new SimpleIntegerProperty();
			//Button
			
		    Button bb = new Button("Generate");
		    bb.setOnAction(event -> {
		    	transitions.clear();
                sq.getChildren().clear();
		        boxes.getChildren().clear();
		        stackPaneList.clear();
		        rect.clear();
	                
		        for (int i = 0; i < numOfNumbers; i++) {
		                int num = random.nextInt(100);
		                Rectangle rectangle = new Rectangle(40, (num * 10)*0.1 + 50);
		                rectangle.setFill(Color.valueOf("#ADD8E6"));
		                rectangle.setArcHeight(20);
		                rectangle.setArcWidth(20);
		                rectangle.setStroke(Color.BLACK);
		                rectangle.setEffect(dropShadow2);
		                rect.add(rectangle);
		                
		                Text text = new Text(String.valueOf(num));
		                StackPane stackPane = new StackPane();
		                stackPane.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
		                stackPane.setId(String.valueOf(num));
		                stackPane.getChildren().addAll(rectangle, text);
		                stackPaneList.add(stackPane);
		            }
		         	boxes.getChildren().addAll(stackPaneList);
		            nextTransitionIndex = new SimpleIntegerProperty();
		            playButton.setDisable(false);
		            startButton.setDisable(false);
		            
		            setTextOff(SelectionSort, selectedAlgo);
		            setTextOff(MergeSort, selectedAlgo);
		            setTextOff(BubbleSort, selectedAlgo);
		            setTextOff(BucketSort, selectedAlgo);
		        });
		        
			playButton = new Button("Play Forward");
	        playButton.setOnAction(event -> {
	        	int index = nextTransitionIndex.get();
	            Animation anim = (Animation) transitions.get(index);
	            anim.setOnFinished(evt -> {
	            	nextTransitionIndex.set(index+1);
	            	if (nextTransitionIndex.get() >= transitions.size())
	            		playButton.setDisable(true);
	            		startButton.setDisable(true);
	            	});
	            anim.setRate(1);
	            anim.play();
	        });
	        playButton.setDisable(true);
	        
	        
	        Button runButton = new Button("Run");
	        runButton.setOnAction(event->{
	        	sq.play();
	        });
	        
	        reverseButton = new Button("Play backward");
	        reverseButton.setOnAction(event -> {
	        	int index = nextTransitionIndex.get()-1;
	        	Animation anim = backTransitions.get(index);
	            anim.setOnFinished(evt -> {
	            	nextTransitionIndex.set(index);
	            	if(nextTransitionIndex.get() <= 0)
	            		reverseButton.setDisable(true);});
	            anim.setRate(1);
	            anim.play();
	        });
	        
	        startButton = new Button("Start Sorting");
	        startButton.setOnMouseClicked(event -> {
	        	transitions.clear();
	        	backTransitions.clear();
	        	sq.getChildren().clear();
	        	int[] arr;
	            switch (selectedAlgo) {
	                case 0:
	                    arr = generateArray(stackPaneList);  
	                    transitions = BubbleSort(arr, stackPaneList,sq, backTransitions);
	                    break;
	                case 1:
	                    arr = generateArray(stackPaneList);
	                    transitions = SelectionSort(arr, stackPaneList, sq, backTransitions);                    
	                    break;
	                case 2:
	                    arr = generateArray(stackPaneList);
	                    BucketSort(arr, stackPaneList, transitions, backTransitions, sq);
	                    break;
	                case 3:
	                	arr = generateArray(stackPaneList);
	                    MergeSort(arr, 0, arr.length-1, stackPaneList, transitions, backTransitions, sq);
	                    break;
	                default:
	                    break;               
	            };
	            startButton.setDisable(true);
	       });
	        
	        startButton.setOnAction(event->{
	        	playButton.setDisable(false);
	        });
	      
	        
	        HBox controls = new HBox(5);
	        controls.setAlignment(Pos.CENTER);
	        controls.getChildren().addAll(textField, button, bb, startButton, playButton, reverseButton, runButton);
	        controls.setPadding(new Insets(0, 0, 20, 0));

	        
	        BorderPane root = new BorderPane();
			
			
			HBox topBox = new HBox(40);
	        topBox.setAlignment(Pos.BASELINE_CENTER);
	        BubbleSort = new Text("Bubble Sort");
	        SelectionSort = new Text("Selection Sort");
	        BucketSort = new Text("Bucket Sort");
	        MergeSort = new Text("Merge Sort");
	        topBox.getChildren().addAll(BubbleSort, SelectionSort, MergeSort, BucketSort);
	        topBoxStyle(topBox);
	        
	        root.setBottom(controls);
			root.setCenter(boxes);
	        root.setTop(topBox);
	        
	        
	        BubbleSort.setOnMouseClicked(event -> {
	        	startButton.setDisable(false);
	            topBoxStyle(topBox);
	            selectedAlgo = 0;
	            BubbleSort.setStyle("-fx-underline:true; \n"+"-fx-font: 20px Tahoma;\n" +
	                    "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
	                    "    -fx-stroke: grey;\n" +
	                    "    -fx-stroke-width: 1;");
	        });
	        SelectionSort.setOnMouseClicked(event -> {
	        	startButton.setDisable(false);
	            topBoxStyle(topBox);
	            selectedAlgo = 1;
	            SelectionSort.setStyle("-fx-underline:true; \n"+"-fx-font: 20px Tahoma;\n" +
	                    "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
	                    "    -fx-stroke: grey;\n" +
	                    "    -fx-stroke-width: 1;");
	        });
	        BucketSort.setOnMouseClicked(event -> {
	        	startButton.setDisable(false);
	            topBoxStyle(topBox);
	            selectedAlgo = 2;
	            BucketSort.setStyle("-fx-underline:true; \n"+"-fx-font: 20px Tahoma;\n" +
	                    "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
	                    "    -fx-stroke: grey;\n" +
	                    "    -fx-stroke-width: 1;");
	        });
	        MergeSort.setOnMouseClicked(event -> {
	        	startButton.setDisable(false);
	            topBoxStyle(topBox);
	            selectedAlgo = 3;
	            MergeSort.setStyle("-fx-underline:true; \n"+"-fx-font: 20px Tahoma;\n" +
	                    "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
	                    "    -fx-stroke: grey;\n" +
	                    "    -fx-stroke-width: 1;");
	        });
	        
	        
	        
			Scene scene = new Scene(root,1000,600);
			primaryStage.setTitle("Sort Algorithms");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void topBoxStyle(HBox topBox) {
        for (int i = 0; i < 4; i++) {
            topBox.getChildren().get(i).setStyle("-fx-font: 20px Tahoma;\n" +
                    "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
                    "    -fx-stroke: grey;\n" +
                    "    -fx-stroke-width: 1;");
        }
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
