
<template>
  <div id="garage">
    <app-header></app-header>
    <p>
      <button v-on:click="sendVehicles()">Export</button>
      <button v-on:click="  xlsVehicles()">  Export XLS</button>
    </p>
    <vehicle-form v-model="vehicle"
                  :makes="makes"
                  :models="models"
                  :drivers="drivers"
                  @submit="submitNewVehicle()">

    </vehicle-form>
    <vehicle-table :vehicles="vehicles"
                    :makes="makes"
                    :models="models"
                    :drivers="drivers"
                    @reload="fetchVehicles()"
     ></vehicle-table>

    <Pagination
      :maxVisibleButtons=3
      :totalPages="numberOfPages"
      :total="total"
      :currentPage="currentPage"
      @pagechanged="pagechanged"/>
  </div>
</template>

<script>
import AppHeader from './AppHeader'
import GarageService from '@/services/GarageService'
import VehicleForm from './form/VehicleForm'
import VehicleTable from './table/VehicleTable'
import Pagination from './Pagination'
export default {
  components: {
    AppHeader,
    VehicleForm,
    VehicleTable,
    Pagination
  },
  data: function () {
    return {
      vehicles: [],
      vehicle: {name: '', make: {id:null}, model: {id:null}, driver: {id:null}},
      models: [],
      makes: [],
      newName: '',
      drivers: [],
      serverURL: process.env.SERVER_URL,
      total: 0,
      max: 10,
      currentPage:1,
      numberOfPages:0,
    }
  },
  // end::component[]
  // tag::fetch[]
  created () { // <1>
    this.fetchData()
  },
  methods: {
    fetchData: async function () {
      try {
        Promise.all([
          this.fetchVehicles(),
          this.fetchModels(),
          this.fetchModels(),
          this.fetchMakes(),
          this.fetchDrivers()
        ])
      } catch (error) {
        console.log(error)
      }
    },
    pagechanged: function(page) {
      console.log("Page = "+page)
      this.currentPage = page;
      this.fetchVehicles((page*this.max)-this.max)
    },
    fetchVehicles: function (pageNumber) {
      //if (pageNumber<=this.total) {
        console.log("Fetching vehicles "+pageNumber)
//,{offset:pageNumber, max:this.max}
        return GarageService.fetchName('vehicle?offset='+pageNumber)
          .then((res) => {
          if (res) {
            if (res.data.objects) {
              console.log("rrrr "+JSON.stringify(res.data.objects))
              this.vehicles = res.data.objects;
              this.total=res.data.total;
              this.numberOfPages=res.data.numberOfPages;
            } else {
              if (res.data) {
                console.log("rr "+res.data)
                this.vehicles = res.data;

              }
            }

          }
        });
      //}

    },
    fetchModels: function () {
      return GarageService.fetchName('model')
        .then((res) => {
        if (res) {
          if (res.data) {
            this.models = res.data;

          }
        }
      });
    },
    fetchMakes: function () {
      return GarageService.fetchName('make')
        .then((res) => {
        if (res) {
          if (res.data) {
            this.makes = res.data;

          }
        }
      });
    },
    sendVehicles: function () {
      console.log('Trying to export vehicles')
      return GarageService.fetchName('exportVehicle')
        .then((res) => {
        if (res) {
          if (res.data) {
          //  this.makes = res.data;
            //console.log(' '+res.data)
            var data, filename, link,dt;
            filename = 'export.csv';
            dt = 'data:text/csv;charset=utf-8,' + res.data;
            data = encodeURI(dt);
            link = document.createElement('a');
            link.setAttribute('href', data);
            link.setAttribute('download', filename);
            link.click();
          }
        }
      });
    },
    xlsVehicles: function () {
      console.log('Trying to export vehicles XLS')
      return GarageService.fetchBlob('exportXls')
        .then((res) => {
        if (res) {
          const url = window.URL.createObjectURL(new Blob([res.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'file.xls');
          document.body.appendChild(link);
          link.click();
        }
      });
    },
    fetchDrivers: function () {
      return GarageService.fetchName('driver')
        .then((res) => {
        if (res) {
          if (res.data) {
            this.drivers = res.data;

          }
        }
      });

    },

    submitNewVehicle: function () {
      const vehicle = {
        vehicle: this.vehicle
      }
      ///console.log(JSON.stringify({name:vehicle.name, make:{id:vehicle.make}, model:{id:vehicle.model}, driver:{id:vehicle.driver}})+" "+JSON.stringify(vehicle));
      console.log(' > '+JSON.stringify(vehicle))
      return GarageService.createName('vehicle',this.vehicle)
        .then((res) => {
        if (res) {
          if (res.data) {
            this.vehicles.push(res.data)
            this.vehicle = {name: '', make: null, model: null, driver: null}

          }
        }
      });
    },
    updateItem () {
      const newName = this.newName;
      console.log('updating item')
      return GarageService.update('vehicle/'+this.item.id, newName)
        .then((res) => {
        if (res) {
          if (res.data) {
            console.log(' data '+res.data)
            this.vehicles.push(res.data)
            this.vehicle = {name: '', make: null, model: null, driver: null}

          }
        }
      });

    }
  }
}
</script>
<style>
  #garage {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    text-align: center;
    color: #2c3e50;
  }
  .reduceZoom {
    zoom:25.50%;

  }
</style>
