import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

//import AuthService from "../services/auth.service";

import "../Styles/Marketplace.scss";




function Marketplace() {













  return (
    <div class="Marketplace">



    <div class="flipping-card-effect">
        
        
    <section class="section-plans" id="section-plans">
      <div class="u-center-text u-margin-bottom-big">
        <h2 class="heading-secondary">
          Hosting Plans
        </h2>
      </div>

      <div class="row">
        <div class="col-1-of-3">
          <div class="card">
            <div class="card__side card__side--front-1">
              <div class="card__title card__title--1">
                <i class="fas fa-paper-plane"></i>
                <h4 class="card__heading">Basic</h4>
              </div>

              <div class="card__details">
                <ul>
                  <li>1 Website</li>
                  <li>50 GB SSD Storage</li>
                  <li>Unmetered Bandwidth</li>
                  <li>Free SSL Certificate</li>
                  <li>1 Included Domain</li>
                  <li>1 Included Domain</li>
                </ul>
              </div>
            </div>
            <div class="card__side card__side--back card__side--back-1">
              <div class="card__cta">
                <div class="card__price-box">
                  <p class="card__price-only">Only</p>
                  <p class="card__price-value">$2.95/mo*</p>
                </div>
                <a href="#popup" class="btn btn--white">Select</a>
              </div>
            </div>
          </div>
        </div>

        <div class="col-1-of-3">
          <div class="card">
            <div class="card__side card__side--front-2">
              <div class="card__title card__title--2">
                <i class="fas fa-plane"></i>
                <h4 class="card__heading">Plus</h4>
              </div>

              <div class="card__details">
                <ul>
                  <li>Includes Basic Package Features</li>
                  <li>Unlimited Websites</li>
                  <li>Unlimited SSD Storage</li>
                  <li>Unlimited Domains</li>
                  <li>Unlimited Parked Domains</li>
                  <li>Unlimited Sub Domains</li>
                </ul>
              </div>
            </div>
            <div class="card__side card__side--back card__side--back-2">
              <div class="card__cta">
                <div class="card__price-box">
                  <p class="card__price-only">Only</p>
                  <p class="card__price-value">$5.45/mo*</p>
                </div>
                <a href="#popup" class="btn btn--white">Select</a>
              </div>
            </div>
          </div>
        </div>

        <div class="col-1-of-3">
          <div class="card">
            <div class="card__side card__side--front-3">
              <div class="card__title card__title--3">
                <i class="fas fa-rocket"></i>
                <h4 class="card__heading">Pro</h4>
              </div>

              <div class="card__details">
                <ul>
                  <li>Includes Plus Plan Features</li>
                  <li>High Performance</li>
                  <li>2 Spam Experts</li>
                  <li>Free SSL Certificate</li>
                  <li>Domain Privacy</li>
                  <li>Site Backup - CodeGuard Basic</li>
                </ul>
              </div>
            </div>
            <div class="card__side card__side--back card__side--back-3">
              <div class="card__cta">
                <div class="card__price-box">
                  <p class="card__price-only">Only</p>
                  <p class="card__price-value">$13.95/mo</p>
                </div>
                <a href="#popup" class="btn btn--white">Select</a>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="u-center-text u-margin-top-huge">
        <a href="#" class="btn btn--green">Get Started</a>
      </div>
    </section>


        
    </div>

























        
<div class="grid">
  <div class="grid__item">
    <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1519681393784-d120267933ba?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Snowy Mountains"/>
      <div class="card__content">
        <h1 class="card__header"><pre>Amazon</pre>  <pre>Video</pre> </h1>
        <p class="card__text">Look up at the night sky, and find yourself <strong>immersed</strong> in the amazing mountain range of Aspen. </p>
        <button class="card__btn">Comprar <span>&rarr;</span></button>
      </div>
    </div>
  </div>
  <div class="grid__item">
    <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1485160497022-3e09382fb310?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Desert"/>
      <div class="card__content">
        <h1 class="card__header"><pre>Paramount</pre>  <pre>Plus</pre> </h1>
        <p class="card__text">Look up at the night sky, and find yourself <strong>immersed</strong> in the amazing mountain range of Aspen. </p>

        <button class="card__btn">Comprar <span>&rarr;</span></button>
      </div>
    </div>
  </div>
  <div class="grid__item">
    <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons"/>
      <div class="card__content">
        <h1 class="card__header"><pre>Star</pre>  <pre>Plus</pre> </h1>
        <p class="card__text">Look up at the night sky, and find yourself <strong>immersed</strong> in the amazing mountain range of Aspen. </p>

        <button class="card__btn">Proximamente <span>&rarr;</span></button>
      </div>
    </div>
  </div>
</div>




    </div>
  )
}

export default Marketplace