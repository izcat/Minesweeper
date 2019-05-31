package record;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JLabel;
import javax.swing.JPanel;

		
public class Record {
	
	static final int totNum = 7;
	static private String[][] names = new String[3][totNum+1];
	static private int[][] recs = new int[3][totNum+1];
	static private int[] recNum = new int[3];
	final static String path = "records/level";
	
	public Record() {
		
		
		for(int i=0;i<3;i++) {
			try {
				
				File file = new File(path+(i+1)+".rec");
				if(!file.exists()) {
					file.createNewFile();
				}
				
				@SuppressWarnings("resource")
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path+(i+1)+".rec")));
				
				String line = null;
				int k = 0;	 
			    while ((line = in.readLine()) != null) {
					// System.out.println(line);
					String[] str =  line.split(", ");
					names[i][k] = new String();
					names[i][k] = str[0];
					recs[i][k] = Integer.parseInt(str[1]);
						
					// System.out.println(names[i][k]+", time:"+recs[i][k]);
					if(++k>=totNum) {
						break;
					}
			    }

			    recNum[i] = k;
			     
			    //关闭流
			    in.close();
			
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static boolean isNewRecord(int lev, int rec) {
		int rank = recNum[lev];	
	//	recs[lev][rank] = rec;
		
		// no records
		if(rank==0) {
			return true;
		}

		--rank;
		while(rank>=0 && rec<recs[lev][rank]) {
			--rank;
		}
		if(rank+1<totNum) {
			return true;
		}
		return false;
	}

	public static void saveRecord(int lev, String name, int rec) {
		int rank = recNum[lev];
		// names[lev][rank] = name;
		// recs[lev][rank] = rec;
		
		--rank;
		while(rank>=0 && rec<recs[lev][rank]) {
			recs[lev][rank+1] = recs[lev][rank];
			names[lev][rank+1] = names[lev][rank];
			--rank;
		}
		
		names[lev][rank+1] = name;
		recs[lev][rank+1] = rec;
		try {
			if(rank+1<totNum) {
				if(recNum[lev]<totNum) {
					++recNum[lev];
				}
				writeRecord(lev);	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			--recNum[lev];
		}
	}
	
	public JPanel showRecord(int lev) {
		JPanel panel = new JPanel(new GridLayout(recNum[lev]+1, 2));
		panel.add(new JLabel("姓名"));
		panel.add(new JLabel("记录"));
		for(int i=0;i<recNum[lev];i++) {
			panel.add(new JLabel(names[lev][i]));
			panel.add(new JLabel(""+recs[lev][i]));
		}
		return panel;
		
	}
	
	private static void writeRecord(int lev) throws IOException {
		
		File fout = new File(path+(lev+1)+".rec");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
        
        // BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(""));
  
        for(int i=0;i<recNum[lev];i++) {
            //写入相关文件
            out.write(names[lev][i]+", "+recs[lev][i]);
            out.newLine();
        }
        
        //清楚缓存
        out.flush();
        out.close();
		
	}
}
		
