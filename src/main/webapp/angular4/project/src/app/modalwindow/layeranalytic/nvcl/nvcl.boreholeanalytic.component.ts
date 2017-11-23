import {saveAs} from 'file-saver/FileSaver';
import { LayerModel } from '../../../portal-core-ui/model/data/layer.model';
import { OnlineResourceModel } from '../../../portal-core-ui/model/data/onlineresource.model';
import { NVCLBoreholeAnalyticService } from './nvcl.boreholeanalytic.service';
import { Component, Input, AfterViewInit, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/modal-options.class';
import { CookieService } from 'ngx-cookie-service';

@Component({
  templateUrl: './nvcl.boreholeanalytic.component.html',
  providers : [NVCLBoreholeAnalyticService]
})
export class NVCLBoreholeAnalyticComponent implements AfterViewInit, OnInit  {

  @Input() layer: LayerModel;
  public nvclform;
  public algorithms;
  public selectedAlgorithm;
  public classifications;
  public isExistingAlgorithm = true;

  public ngSelectiveConfig = {};
  public ngSelectiveOptions = [];
  public currentStatus = [];

  // VT: object to keep track of the tabpanel
  public UItabpanel = {
    algorithm: true,
    checkprocess: false
  };


  constructor(public nvclBoreholeAnalyticService: NVCLBoreholeAnalyticService, private cookieService: CookieService) {
    this.nvclform = {};
  }


  ngAfterViewInit(): void {
    this.nvclBoreholeAnalyticService.getNVCLAlgorithms().subscribe(results => {
      this.algorithms = results;
      if (this.cookieService.check('email')) {
        this.nvclform.email = this.cookieService.get('email');
      }
    });
  }

  ngOnInit() {
    this.ngSelectiveConfig = {
      labelField: 'label',
      valueField: 'value',
      maxItems: 5
    }
  }


  public changeAlgorithm() {

    this.nvclform.algorithmOutputIds = [];
    this.nvclform.logName = null;
    this.ngSelectiveOptions  = [];
    this.nvclform.classification = null;

    if (this.selectedAlgorithm === 'nsa') {
      return;
    }

    for (const versionlist of this.selectedAlgorithm.versions) {
      this.ngSelectiveOptions.push({
        label: versionlist.version,
        value: versionlist.algorithmOutputId,
        code: versionlist
      });
    }

  }

  public changeTSGAlgorithm() {
    this.nvclBoreholeAnalyticService.getTSGAlgorithm(this.nvclform.tsgAlgName).subscribe(response => {
      this.nvclform.tsgAlgorithm = response;
    })
  }

  public onVersionChange($event) {
    const algorithmOutputIds = $event;
    if (algorithmOutputIds.length <= 0) {
      return;
    }
    this.nvclBoreholeAnalyticService.getNVCLClassifications(algorithmOutputIds).subscribe(classifications => {
      this.classifications = classifications
    })
  }

  public submit() {
    if (this.isExistingAlgorithm) {
      this.nvclform.algorithm = this.selectedAlgorithm.algorithmId;
      this.nvclBoreholeAnalyticService.submitSF0NVCLProcessingJob(this.nvclform, this.layer).subscribe(response => {
        if (response === true) {
          alert('Job have been successfully submitted. The results will be send to your email');
          this.cookieService.set( 'email', this.nvclform.email );
        }
      })
    } else {
      this.nvclBoreholeAnalyticService.submitSF0NVCLProcessingTsgJob(this.nvclform, this.layer).subscribe(response => {
        if (response === true) {
          alert('Job have been successfully submitted. The results will be send to your email');
          this.cookieService.set( 'email', this.nvclform.email );
        }
      })
    }

  }

  public checkStatus() {
    this.nvclBoreholeAnalyticService.checkNVCLProcessingJob(this.nvclform.email).subscribe(response => {
      this.currentStatus = response;
      if (response.length > 0) {
        this.cookieService.set('email', this.nvclform.email);
      }
    });
  }

  public download(jobId: string) {
    this.nvclBoreholeAnalyticService.downloadNVCLProcessingResults(jobId).subscribe(response => {
      const blob = new Blob([response], {type: 'application/zip'});
      saveAs(blob, 'download.zip');
      this.cookieService.set( 'email', this.nvclform.email );
    })
  }


}