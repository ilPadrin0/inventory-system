<template>
  <div>
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Bar } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from "chart.js";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
);

export default {
  name: "StatisticsChart",
  components: { Bar },
  props: {
    products: {
      type: Array,
      required: true,
    },
  },
  computed: {
    chartData() {
      const totalProducts = this.products.length;
      const totalQuantity = this.products.reduce(
        (sum, p) => sum + Number(p.quantity),
        0
      );
      const totalValue = this.products.reduce(
        (sum, p) => sum + Number(p.quantity) * Number(p.price),
        0
      );
      const lowStock = this.products.filter(
        (p) => p.quantity > 0 && p.quantity <= 5
      ).length;
      const noStock = this.products.filter(
        (p) => Number(p.quantity) === 0
      ).length;

      return {
        labels: [
          "전체 상품 수",
          "총 재고 수량",
          "재고 부족 수",
          "재고 없음 수",
          "총 재고 금액",
        ],
        datasets: [
          {
            label: "상품 통계 (개수)",
            data: [totalProducts, totalQuantity, lowStock, noStock, null],
            backgroundColor: "rgba(54, 162, 235, 0.7)",
            yAxisID: "y",
          },
          {
            label: "총 재고 금액 (원)",
            data: [null, null, null, null, totalValue],
            backgroundColor: "rgba(255, 99, 132, 0.7)",
            yAxisID: "y1",
          },
        ],
      };
    },
    chartOptions() {
      return {
        responsive: true,
        interaction: {
          mode: "index",
          intersect: false,
        },
        scales: {
          y: {
            type: "linear",
            position: "left",
            title: {
              display: true,
              text: "개수",
            },
            beginAtZero: true,
          },
          y1: {
            type: "linear",
            position: "right",
            title: {
              display: true,
              text: "금액 (원)",
            },
            beginAtZero: true,
            grid: {
              drawOnChartArea: false,
            },
          },
        },
        plugins: {
          legend: {
            position: "top",
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                const value = context.parsed.y;
                if (value == null) return null;
                return `${context.dataset.label}: ${value.toLocaleString()}`;
              },
            },
          },
        },
      };
    },
  },
};
</script>

<style scoped>
div {
  height: 400px;
  margin-bottom: 20px;
}
</style>
