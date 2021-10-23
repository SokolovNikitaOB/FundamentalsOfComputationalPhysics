package plotting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.*;


public class LineChartEx extends JFrame {

    public LineChartEx(String s, XYDataset dataset, String title, String xAxisLabel, String yAxisLabel, int countGraphs,ValueAxis axis) {
        initUI(s, dataset,title, xAxisLabel, yAxisLabel, countGraphs,axis);
    }

    private void initUI(String name, XYDataset data,String title, String xAxisLabel, String yAxisLabel, int countGraphs,ValueAxis axis) {
        XYDataset dataset = data;
        JFreeChart chart = createChart(dataset, title, xAxisLabel, yAxisLabel, countGraphs,axis);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle(name);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JFreeChart createChart(XYDataset dataset, String title, String xAxisLabel, String yAxisLabel, int countGraphs, ValueAxis axis) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        axis.setLabel(yAxisLabel);
        plot.setRangeAxis(axis);

        XYLineAndShapeRenderer renderer = getRender(countGraphs);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);


        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }

    private static XYLineAndShapeRenderer getRender(int count){
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 0; i < count; i++) {
            renderer.setSeriesShape(i, new Rectangle(-1,-1,0,0));
            if(i == 0){
                renderer.setSeriesPaint(i, new Color(1f,0f,0f));
            }else if(i==1){
                renderer.setSeriesPaint(i, new Color(0f,0f,1f));
            } else {
                renderer.setSeriesPaint(i, new Color(0f,0f,0f));
            }
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }
        return renderer;
    }
}