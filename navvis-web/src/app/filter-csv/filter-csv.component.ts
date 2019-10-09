import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-filter-csv',
  templateUrl: './filter-csv.component.html',
  styleUrls: ['./filter-csv.component.css']
})
export class FilterCsvComponent implements OnInit {

  constructor(public formBuilder: FormBuilder) {

    /**
     * Filter types
     */
    var knn = new FilterType();
    knn.id = 1;
    knn.name = 'Knn';

    var average = new FilterType();
    average.id = 2;
    average.name = 'Average';

    this.form = this.formBuilder.group({
      filterTypes: ['']
    });

    this.filterTypes.push(knn);
    this.filterTypes.push(average);

  }

  form: FormGroup;
  fileInput: any[] = [];
  fileInputCopy: any[];
  rowsWithZero = [];
  filterTypes: any[] = [];
  inputText: String = '';
  outputText: String = '';
  isFileLoaded: boolean = false;
  average: number = 0;

  ngOnInit() {
  }

  handleFiles($event: any) {
    this.outputText = '';
    this.inputText = '';

    /**
     * Read csv and create fileInput array
     */
    let files = $event.srcElement.files;

    let input = $event.target;
    let reader = new FileReader();
    reader.readAsText(input.files[0]);

    reader.onload = () => {
      let csvData = reader.result;

      let csvRecordsArray = (<string>csvData).split(/\r\n|\n/);

      let rowCount = 0;
      let columnCount = 0;
      let sum = 0;
      let countRows = [];

      /**
       * Create fileInput array from csv, keep track of rows with zeros and calculate average
       */
      for (let index = 0; index < csvRecordsArray.length; index++) {
        const row = csvRecordsArray[index];
        this.inputText += row;
        let splittedRow = row.split(/[\s,]+/);
        this.fileInput[rowCount] = [];
        var n = 0;
        /**
         * Rows
         */
        for (let inner = 0; inner < splittedRow.length; inner++) {
          var element = parseInt(splittedRow[inner]);
          if (element == 0) {
            this.rowsWithZero.push(inner);
            n++;
            countRows[inner]++;
          } else {
            sum += element;
          }

          this.fileInput[rowCount][columnCount] = element;
          columnCount++;
        }
        columnCount = 0;
        rowCount++;
        this.inputText += "\n";

        if (n == this.fileInput[index].length) {
          //exception ("The whole row " + i + " is missing");
        }

        if (countRows[index] == this.fileInput.length) {
          //	exception ("The whole column " + i + " is missing");
        }
      }

      this.isFileLoaded = true;
      this.average = sum / (this.fileInput[0].length * this.fileInput.length);

      /**
       * Create a safe copy
       */
      this.fileInputCopy = JSON.parse(JSON.stringify(this.fileInput));
    };
  }


  filterData() {
    this.outputText = '';
    this.fileInput = JSON.parse(JSON.stringify(this.fileInputCopy));

    switch (this.form.get('filterTypes').value) {
      /**
       * Knn
       */
      case '1':

        let nosRows = this.fileInput[0].length;
        let nosCols = this.fileInput.length;
        let items = nosRows * nosCols;
        /**
         * Sample of neighbors
         */
        let k = Math.sqrt(items);

        for (let withZero = 0; withZero < this.rowsWithZero.length; withZero++) {
          var map = [];

          var x = this.fileInput[this.rowsWithZero[withZero]];

          /**
           * Create a distance map
           */
          var distanceMap = [];
          for (var j = 0; j < this.fileInput.length; j++) {

            var y = this.fileInput[j];
            var distObj = new Distance();
            distObj.position = j;
            distanceMap[j] = distObj;
            for (var m = 0; m < x.length; m++) {
              if (x[m] != 0 && y[m] != 0) {
                var d: number = x[m] - y[m];
                distObj.distance = distanceMap[j].distance + (d * d);
                distanceMap[j] = distObj;
              }
            }
          }

          /**
           * Sort distance map and create index of neighbors to get K neighbors
           */
          distanceMap.sort((a, b) => (a.distance > b.distance) ? 1 : -1)

          var neighbors = [];

          var ktmp = k;

          while (ktmp > 0) {

            for (var j = 0; j < distanceMap.length; j++) {
              neighbors.push(distanceMap[j].position);
              ktmp--;
            }
          }

          /**
           * Replace zeros with average of neighbors
           */
          for (var j = 0; j < x.length; j++) {
            if (x[j] == 0) {
              var n = 0;
              var m = 0;
              var replace: number = 0;
              while (m < this.fileInput.length) {

                for (var neighbor = 0; neighbor < neighbors.length; neighbor++) {

                  if (this.fileInput[neighbors[neighbor]][m] != 0) {
                    replace = replace + parseInt(this.fileInput[neighbors[neighbor]][m]);
                    n++;
                  }
                }
                m++;
              }

              x[j] = replace / n;
            }
          }

          for (var arr in this.fileInput) {
            this.outputText += this.fileInput[arr].toString() + "\n";
          }
        }

        break;

      /**
       * Average
       */
      case '2':
        for (let withZero = 0; withZero < this.rowsWithZero.length; withZero++) {

          var x = this.fileInput[this.rowsWithZero[withZero]];
          /**
           * Replace zeros with average
           */
          for (var j = 0; j < x.length; j++) {
            if (x[j] == 0) {
              x[j] = this.average;
            }
          }
        }

        for (var arr in this.fileInput) {
          this.outputText += this.fileInput[arr].toString() + "\n";
        }

        break;

      default:
        break;
    }
  }


}

class Distance {
  position: number = 0;
  distance: number = 0;
}

class FilterType {
  id: number;
  name: string;
}