<template>
  <div id="custom">
    <app-header
      header-image="retail"
      header-text="Arrrr welcome to the shop"
    ></app-header>

    <span v-if="enableSearch" >
    <search-form v-model="search"
                 :makes="makes"
                 :actualItem="item"
                 :hide-search="hideSearch"
                 :models="models"
                 @submit="searchVehicles()"/>

    <custom-table :vehicles="realVehicles"
                  :makes="makes"
                  :models="models"
                  @update-vehicles="updateVehicleContent"
                  @reloadVehicles="searchVehicles()"
                  v-bind="{fetchVehicles,sortSearch}"
    ></custom-table>
    </span>
    <span v-else>

          <div class="container">
            Fantastic vehicle offers

          <div  class="content" v-for="(f, index) in availableHires">
             <div class="col-md-3">
              <h4 class="text-success">
                <span class="label label-primary pull-right">
                Make: {{ f.makeName }}  Model: {{f.modelName}}<br/> <br/>
                Min Cost: {{ f.minCost }}  Max Cost: {{f.maxCost}}<br/>
                Min Deposit: {{ f.minDeposit }}  Max Deposit: {{f.maxDeposit}}<br/>
                <br/>
                   <span  v-on:click="activateSearch(f)" class="lookupDeal btn btn-xs btn-danger pull-right">
                     Lookup deals
                   </span>
                </span>
              </h4>
            </div>
            <div v-bind:id="`container-${index}`" ></div>
        </div>
     </div>

    </span>
  </div>
</template>
<script>
  import $ from 'jquery';
  import AppHeader from '../AppHeader'
  import SearchForm from './SearchForm'
  import GarageService from '@/services/GarageService'
  import CustomTable from './RentalVehicleTable'
  import Pagination from '../Pagination'
  import moment from 'moment';
  import { mapGetters } from 'vuex'

  export default {
    props: ['searchDetails','counter'],
    components: {
      SearchForm,
      AppHeader,
      CustomTable,
      Pagination,
      moment
    },
    data: function () {
      return {
        availableHires:[],
        //vehicles: this.$store.getters.vehicles,
        //total: this.$store.getters.total,
       // numberOfPages: this.$store.getters.numberOfPages,
        search:{contractName:'',cost:'',deposit:'', vehicleName: '', make: {id:null}, model: {id:null}, driver: {id:null},returnDate1:'',fromDate1:'',toDate1:''},
        showSearch:false,
        vehicle: {},
        contracts: [],
        item:{
          make: {id: null,name:''},
          model: {id: null,name:''},
        },
        models: [],
        makes: [],
        newName: '',
        drivers: [],
        serverURL: process.env.SERVER_URL,
        max: 10,
        offset: 0,
        currentPage: 1,
        //numberOfPages: this.$store.getters.numberOfPages,
        currentSort: '',
        currentSortDir: 'asc',
        lastPath:'',
        lastCounter:0,
      }
    },
    // end::component[]
    // tag::fetch[]
    created: function () {	//#E
      ///this.showSearch=this.searchDetails;
      console.log('created show search'+this.showSearch);
      this.fetchData()

    },
    mounted: function () {	//#E
      console.log('mounted show search'+this.showSearch);
      console.log('Vehicles when mounted'+this.$store.getters.vehicles);


    },
    beforeMount: function () {	//#D
     // this.showSearch = this.searchDetails;
        console.log(" beforeMount");	//#D
      this.$eventHub.$on('rentalcounter', function(newCounter){
        //if (newCounter > counter) {
        console.log('rentalcounter before',newCounter+' '+this.showSearch);
        this.$emit('searchDetails',false);
        //this.$emit('rentalcc',newCounter);
        //setTimeout(() => {this.showSearch=''}, 0)
        //  this.counter=newCounter;
        //this.hideSearch();
        //}
        this.showSearch=false
        //console.log('rentalcounter after',newCounter+' '+this.showSearch);

      })

    },	//#D

  computed:{
    ...mapGetters([
      'loadVehicles',
      'total',
        'numberOfPages'

    ]),
    // Create another computed property to call your mapped getter while passing the argument
    realVehicles () {
     // setTimeout(() => {return this.loadVehicles}, 1000)
      //setTimeout(() => {return this.$store.getters.loadVehicles()}, 1000)

      return this.loadVehicles;

    },
    // Create another computed property to call your mapped getter while passing the argument
    realTotal () {
      return this.total;
    },
    // Create another computed property to call your mapped getter while passing the argument
    realPages () {
      return this.numberOfPages;
    },


    //This is now ignored this was sorting based on paginated data
    //simply replace vehicle in vehicles to vehicle in sortedCats above line 14/15
    enableSearch() {
      console.log(' +'+this.searchDetails+' '+this.showSearch)
      return this.showSearch===true;
      }
    },
  beforeUpdate: function () { 	//#F
    //this.showSearch=this.searchDetails
    console.log('beforeUpdate show search'+this.showSearch);
    // this.showSearch = this.searchDetails;
    //this.showSearch = this.searchDetails;

  },	//#
  mounted() {

  },
  	//#
    methods: {
      hideSearch: function() {
        console.log('hiding')
        this.showSearch=false;
      },
      //this.fetchModels(),
      // this.fetchModels(),
      //this.fetchMakes(),
      fetchData: async function () {
        try {
          Promise.all([
            //this.fetchVehicles(0),
            this.listDefaults(),


          ])
        } catch (error) {
          console.log(error)
        }
      },
      initialiseVehicles(params){
        //This is called in vehicles.js within store/modules and params passed direct
        this.$store.dispatch( {type:'initVehicles',params:params});
       // console.log('--> store vehicles ='+JSON.stringify(this.$store.getters.loadVehicles()));
        //setTimeout(() => {console.log('--> store vehicles ='+JSON.stringify(this.$store.getters.loadVehicles()))}, 1000)


        //no longer used
        /*
        return GarageService.fetchRoot('/guest/rental?'+params)
          .then((res) => {
          if (res) {
            console.log(' rees'+JSON.stringify(res))
            if (res.data.instanceList) {
              console.log("rr "+res.data.instanceList)
              this.vehicles = res.data.instanceList;
              this.total=res.data.instanceTotal;
              this.numberOfPages=res.data.numberOfPages;
            } else {
              if (res.data) {
                //console.log("rr "+res.data.objects)
                this.vehicles = res.data;

              }
            }

          }
        });
        */
      },
      activateSearch(vehicle) {
        this.showSearch=true;
        this.item.make.id=vehicle.makeId;
        this.item.make.name=vehicle.makeName;
        this.item.model.id=vehicle.modelId;
        this.item.model.name=vehicle.modelName;
        setTimeout(() => {this.searchVehicles()}, 100);

      },
      pagechanged: function(page) {
        console.log("Page = "+page)
        this.currentPage = page;
        this.offset=(page*this.max)-this.max
        this.fetchVehicles(this.offset)
      },
      sortSearch(currentSort,currentSortDir) {
        //This is coming back grom VehicleTable sortable column full sorting is method 2 here
        var variables = $.param(this.search);
        if (this.search.returnDate1) {
          variables+="&returnDate="+moment(this.search.returnDate1).format('DD MMM YYYY')
        }
        this.currentSort=currentSort;
        this.currentSortDir=currentSortDir;
        variables+="&sort="+currentSort+"&order="+currentSortDir+'&offset='+ this.offset;
        this.initialiseVehicles(variables);
      },
      fetchVehicles: function (pageNumber) {
        var variables=''
        if (this.search) {
          variables += $.param(this.search);
          if (this.search.returnDate1) {
            variables+="&returnDate="+moment(this.search.returnDate1).format('DD MMM YYYY')
          }

        }
        if (this.currentSort) {
          variables+="&sort="+this.currentSort+"&order="+this.currentSortDir;
        }
        if (variables!='') {
          variables+='&offset='+pageNumber
        } else {
          variables='?offset='+pageNumber
        }

        console.log("Fetching vehicles "+pageNumber)
        this.initialiseVehicles(variables);
      },

      updateVehicleContent: function(cv) {
        //This is the vehicle object which is emitted by RentalVehicleTable to this parent page -
        // This now calls vuex storage and updates cache value with the new vehicle in its existing array of vehicles
        console.log('updateing vehicle ---<<<<'+cv.id)
        this.$store.dispatch( {type:'updateVehicles',vehicle:cv});
      },
      searchVehicles: function () {
        //console.log("searching vehicles ------------ "+this.search.make)
        console.log("searching vehicles "+$.param(this.search))
        var variables = $.param(this.search);
        if (this.search.returnDate1) {
          variables+="&returnDate="+moment(this.search.returnDate1).format('DD MMM YYYY')
        }
        if (this.currentSort) {
          variables+="&sort="+this.currentSort+"&order="+this.currentSortDir;
        }
        this.initialiseVehicles(variables);
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
        console.log('getting makes ------------------------');
        return GarageService.fetchName('make')
          .then((res) => {
          if (res) {
            if (res.data) {
              this.makes = res.data;

            }
          }
        });
      },
      listDefaults: function () {
        return GarageService.fetchRoot('/guest/availableHires')
          .then((res) => {
          if(res) {
            if (res.data) {
              this.availableHires = res.data;

            }
          }
        }
      );

      }

    }
  }
</script>
<style>
  #custom {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    text-align: center;
    color: #2c3e50;
  }
  .reduceZoom {
    zoom:25.50%;

  }
</style>
