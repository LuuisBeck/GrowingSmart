package com.example.h2ces.growingsmart;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class GraphActivity extends Activity {

    LineChart graph;
    String graphType;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graph = findViewById(R.id.linechart);
        // graph.setOnChartGestureListener(MainActivity.this);
        // graph.setOnChartValueSelectedListener(MainActivity.this);

        graphType = getIntent().getStringExtra("graphType");
        if (graphType.equals("temperatura")){
            createLinealGraph(graph, "T° en tiempo real (°C)");
        }
        else if (graphType.equals("humedad")){
            createLinealGraph(graph, "Humedad ambiente (%)");
        }
        else{
            createLinealGraph(graph, null);
        }


    }

    public void createLinealGraph(LineChart graph, String title){
        graph.setDragEnabled(true);
        graph.setScaleEnabled(false);


        ArrayList<Entry> arrayData = getSensorDatabase("temperature");


        /* Quita label derecho*/
        graph.getAxisRight().setEnabled(false);

        /* Agrega limite */
        LimitLine upper_limit = new LimitLine(70, "T° ideal");
        upper_limit.setLineWidth(4);
        upper_limit.enableDashedLine(10, 5, 10);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(15);

        YAxis leftAxis = graph.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.setTextSize(15f);

        /* Valores de x,y para gráfico */
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 60f));
        values.add(new Entry(1, 70f));
        values.add(new Entry(2, 80f));
        values.add(new Entry(3, 90f));
        values.add(new Entry(4, 95));
        values.add(new Entry(5, 110f));
        values.add(new Entry(6, 110));

        /* Configuración de gráfico */
        LineDataSet set1 = new LineDataSet(arrayData, title);
        set1.setFillAlpha(110);
        set1.setLineWidth(6f);
        set1.setCircleColor(Color.RED);
        set1.setCircleRadius(5f);
        set1.setValueTextSize(15f);

        /* Es posible agregar más de un dato*/
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        /* Se guarda todo en graph */
        LineData data = new LineData(dataSets);
        graph.setData(data);
        graph.getDescription().setText("Hora");
        graph.getDescription().setTextSize(15f);
        graph.getDescription().setTextColor(Color.RED);


        String[] valuesx = lastNhoursString(7);
        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);
        xAxis.setValueFormatter(new MyAxisValueFormatter(valuesx));
        xAxis.setGranularity(1);

        Legend legend = graph.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        legend.setTextSize(20f);

    }

    private ArrayList<Entry> getSensorDatabase(String type) {
        final ArrayList<Entry> temperature = new ArrayList<>();
        final ArrayList<Entry> humidity = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference();
        Query sensor = ref.child("sensor").child("dht1")
                .orderByKey().limitToLast(7);
        sensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot temp: dataSnapshot.getChildren() ){
                    String T = temp.child("temperature1").getValue().toString();
                    String H = temp.child("humidity1").getValue().toString();
                    temperature.add(new Entry(i++,Integer.parseInt(T)));
                    humidity.add(new Entry(i++, Integer.parseInt(H)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (type.equals("temperature")){
            return temperature;
        }
        else{
            return humidity;
        }
    }

    public class MyAxisValueFormatter implements IAxisValueFormatter {

        private String[] valuesX;

        public MyAxisValueFormatter(String[] values){
            this.valuesX = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return valuesX[Math.round(value)];
        }
    }

    public String[] lastNhoursString(int n){
        ArrayList<String> array = new ArrayList<>();

        DateTime dt = new DateTime();
        int hour = dt.getHourOfDay();
        int minutes = dt.getMinuteOfHour();

        for (int i=(n-1); i>=0; i--){
            StringBuilder stringBuilder = new StringBuilder();
            int j = ((((hour - i) % 24) + 24) % 24);
            stringBuilder.append(Integer.toString(j));
            stringBuilder.append(":");
            stringBuilder.append(Integer.toString(minutes));
            array.add(stringBuilder.toString());

        }

        return array.toArray(new String[0]);
    }

}
