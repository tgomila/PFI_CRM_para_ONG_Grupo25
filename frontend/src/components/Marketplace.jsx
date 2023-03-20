import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

//import AuthService from "../services/auth.service";

import "../Styles/Marketplace.scss";




function Marketplace() {



  return (
    <div class="Marketplace">
      <div class="grid">
        <div class="grid__item">
          <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1519681393784-d120267933ba?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Snowy Mountains" />
            <div class="card__content">
              <h1 class="card__header">7 Dias Gratis</h1>
              <p class="card__text">Look up at the night sky, and find yourself <strong>immersed</strong> in the amazing mountain range of Aspen. </p>
              <button class="card__btn">Comprar <span>&rarr;</span></button>
            </div>
          </div>
        </div>
        <div class="grid__item">
          <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1485160497022-3e09382fb310?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Desert" />
            <div class="card__content">
              <h1 class="card__header">Plan total 1 mes</h1>
              <p class="card__text">Capture the stunning <strong>essence</strong> of the early morning sunrise in the Californian wilderness.</p>
              <button class="card__btn">Comprar <span>&rarr;</span></button>
            </div>
          </div>
        </div>
        <div class="grid__item">
          <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
            <div class="card__content">
              <h1 class="card__header">Plan total 12 meses</h1>
              <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
              <button class="card__btn">Proximamente <span>&rarr;</span></button>
            </div>
          </div>
        </div>


        {/*  */}


        </div>


        <div class="container mx-auto">
          <div class="row">
            <div class="col-lg-4 col-md-12">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Actividad</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn">Proximamente <span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Factura</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn">Proximamente <span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Insumo</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn">Proximamente <span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-lg-4 col-md-12">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Prestamo</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn">Proximamente <span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Proyecto</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn">Proximamente <span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Chat</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn">Proximamente <span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>


    </div>
  )
}

export default Marketplace