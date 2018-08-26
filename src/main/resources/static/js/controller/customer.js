angular.module('appCustomer',['ngMessages']).controller('customerController', ['$scope', '$http',function(vm, $http){ 

	var vm = this;
	
	vm.customers = [];
	vm.currentPage= 0;
	vm.totalPages= 0;
	vm.firstNameFilter= "";
	vm.lastNameFilter= "";
	vm.btnNext=false;
	vm.btnPrevious=true;
	vm.showForm=false;
	vm.typeAddressToRemove = "";
	vm.showMessageSuccess = false;
	vm.erroAddressSubmit = false;
	vm.erroAddressAdd = false;
	vm.erroAddress = false;
	vm.erroZipCode = false;
	vm.erroCity = false;
	vm.erroCountry = false;
	

	vm.customer = {
			id: -1,
			firstName: "",
			lastName: "",
			phoneNumber: "",
			address: []
	};
	
	vm.address = {
			address:"",
			number:"",
			zipCode:"",
			city:"",
			country:"",
			type:""
	};
	
	<!-- LIST --!>  
	
	getCustomers();

	function getAddressTypes() {
		$http({
			method: 'GET',
			url: '/addressTypes'
		}).then(
				function(res) { // success
					vm.addressTypes = res.data;
				},
				function(res) { // error
					console.log("Error: " + res.status + " : " + res.data);
				}
		);
	};

	function getCustomers() {
		$http({
			method: 'GET',
			url: '/customers/' + vm.currentPage,
			params: {firstNameFilter: vm.firstNameFilter, lastNameFilter: vm.lastNameFilter}

		}).then(
				function(res) { // success
					vm.customers = res.data.content;
					vm.totalPages = res.data.totalPages;
					managerPagination();
				},
				function(res) { // error
					console.log("Error: " + res.status + " : " + res.data);
				}
		);
	};

	vm.filter = function() {
		vm.currentPage= 0;
		vm.customers = [];
		getCustomers()
	}

	vm.nextPage = function() {
		vm.currentPage = vm.currentPage + 1
		vm.showMessageSuccess = false;
		vm.customers = [];
		getCustomers()
		managerPagination();
	}

	vm.previousPage = function() {
		vm.currentPage = vm.currentPage - 1
		vm.showMessageSuccess = false;
		vm.customers = [];
		getCustomers()
		managerPagination();
	}
	
	function managerPagination() {
		if(vm.currentPage == 0){
			vm.btnPrevious = true;
		} else {
			vm.btnPrevious = false;
		}
		if(new Number(vm.currentPage + 1) < new Number(vm.totalPages)){
			vm.btnNext = false;
		} else {
			vm.btnNext = true;
		}
	}
	
	<!-- INSERT --!> 
	
	vm.createCustomer = function() {
		getAddressTypes();
		_clearFormData();
	}
	
	vm.addAddress = function(){
		if(isFormAddressValid()){
			vm.customer.addresses.push({id:vm.address.id,
				address: vm.address.address,
				number: vm.address.number,
				zipCode: vm.address.zipCode,
				city: vm.address.city,
				country: vm.address.country,
				type: vm.address.type}),
			vm.typeAddressToRemove= vm.address.type, 
			vm.address.address ="",
			vm.address.number ="",
			vm.address.zipCode="",
			vm.address.city="",
			vm.address.country="",
			vm.address.type="",
			vm.addressTypes.splice(vm.addressTypes.indexOf(vm.typeAddressToRemove), 1);
		}
	}
	
	function isFormAddressValid(){
		vm.erroAddressSubmit = false;
		vm.erroAddressAdd = false;
		vm.erroAddress = false;
		vm.erroZipCode = false;
		vm.erroCity = false;
		vm.erroCountry = false;
		
		if(vm.address.type == ''){
			vm.erroAddressAdd = true;
		}
		
		if(vm.address.address == ''){
			vm.erroAddressAdd = true;
			vm.erroAddress = true;
		}
		if(vm.address.zipCode == ''){
			vm.erroZipCode = true;
		}
		if(vm.address.city == ''){
			vm.erroCity = true;
		}
		if(vm.address.country == ''){
			vm.erroCountry = true;
		}
		if(vm.erroAddressSubmit || vm.erroAddressAdd 
				|| vm.erroAddress || vm.erroZipCode
				|| vm.erroCity || vm.erroCountry){
			return false;
		} 
		return true;
	}
	
	vm.deleteAddress = function(addressToRemove){
		vm.addressTypes.push(addressToRemove.type);
		vm.customer.addresses.splice(vm.customer.addresses.indexOf(addressToRemove), 1);
	}
	
	<!-- EDIT --!> 
	
	function getCustomer(idCustomer) {
		$http({
			method: 'GET',
			url: '/customer/' + idCustomer,

		}).then(
				function(res) { // success
					vm.customer = res.data;
				},
				function(res) { // error
					console.log("Error: " + res.status + " : " + res.data);
				}
		);
	};
	
	vm.editCustomer = function(newCustomer) {
		getCustomer(newCustomer.id);
		_clearFormAddress();
		vm.showForm = true;
		vm.addressTypes =[];
	};
	
	vm.editAddress = function(newAddress) {
		vm.deleteAddress(newAddress);
		vm.address.id =newAddress.id;
		vm.address.type =newAddress.type;
		vm.address.address =newAddress.address;
		vm.address.number =newAddress.number;
		vm.address.zipCode=newAddress.zipCode;
		vm.address.city=newAddress.city;
		vm.address.country=newAddress.country;
		
	};
	
	
	vm.submitCustomer = function() {

		var method = "";
		var url = "";

		if (vm.customer.id == -1) {
			method = "POST";
			url = '/customer';
		} else {
			method = "PUT";
			url = '/customer';
		}
		if(isFormValid()){
			$http({
				method: method,
				url: url,
				data: angular.toJson(vm.customer),
				headers: {
					'Content-Type': 'application/json'
				}
			}).then(_success, _error);
		}
	};
	
	function isFormValid(){
		vm.erroAddressSubmit = false;
		vm.erroAddressAdd = false;
		if(vm.customer.addresses == ""  || vm.customer.addresses.length < 3){
			vm.erroAddressSubmit = true;
			vm.messageErro = vm.messageErro + "Billing, Shipping and Default Addresses are required.<br>";
		}
		return !vm.erroAddressSubmit;
	}

	<!-- DELETE --!> 
	vm.deleteCustomer = function(customer) {
		$http({
			method: 'DELETE',
			url: '/customer/' + customer.id
		}).then(_success, _error);
	};


	function _success(res) {
		vm.showForm = false;
		form.$submitted = false;
		getCustomers();
		_clearFormData();
		vm.showMessageSuccess = true;
	}

	function _error(res) {
		var data = res.data;
		var status = res.status;
		var header = res.header;
		var config = res.config;
		alert("Error: " + status + ":" + data);
	}

	function _clearFormData() {
		_clearFormAddress();
		vm.customer.id = -1;
		vm.customer.firstName = "";
		vm.customer.lastName = "";
		vm.customer.phoneNumber = "";
		vm.customer.addresses= [];
		form.$submitted = false;
		vm.erroAddressSubmit = false;
		vm.erroAddressAdd = false;
		vm.erroAddress = false;
		vm.erroZipCode = false;
		vm.erroCity = false;
		vm.erroCountry = false;
		
	};
	
	function _clearFormAddress() {
		vm.address.address ="";
		vm.address.number ="";
		vm.address.zipCode="";
		vm.address.city="";
		vm.address.country="";
		vm.address.type="";
		vm.address.id="";
	}
}]);