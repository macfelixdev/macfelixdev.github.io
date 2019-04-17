"use strict";

// Gloabl variables
let totalCoin = 0;
let timeOutVal = 5000;
let priceOrganic = 2;
let priceCaramel = 2.5;
let priceHazelnut = 3.1;
let displayScreen = document.getElementById("displayScreen");
let coinSlot = document.getElementById('coinSlot');
let selOrganic = document.getElementById('selOrganic');
let selCaramel = document.getElementById('selCaramel');
let selHazelnut = document.getElementById('selHazelnut');
let divWelcome = document.getElementById("divWelcome");

// Add event listeners
document.getElementById('dropCoin').addEventListener('click', evaluateCoin);
document.getElementById('cancelButton').addEventListener('click', exitTransaction);
selOrganic.addEventListener('click', evaluateSelection);
selCaramel.addEventListener('click', evaluateSelection);
selHazelnut.addEventListener('click', evaluateSelection);
coinSlot.addEventListener('keyup',identifyEnterKey);

disableSelections();

/******************************************************************************/
/* PRESENTATION LOGIC                                                         */
/******************************************************************************/

// Display welcome message
function displayWelcome(){
  divWelcome.style.display = "block";
}

// Hide welcome message
function hideWelcome(){
  divWelcome.style.display = "none";
}

// Disable selections
function disableSelections(){
  selOrganic.disabled = true;
  selCaramel.disabled = true;
  selHazelnut.disabled = true;
}

// Clear coin slot.
function clearCoinSlot(){
  coinSlot.value = null;
}

// Remove message from display screen.
function removeDisplayMsg(msgDiv){
  msgDiv.remove();
  displayWelcome();
}

// Display coin meter
function displayCoinMeter(){
  document.getElementById("coinMeter").innerText = 'Total: $' + totalCoin.toFixed(2);
}

// Allow enter key to trigger evaluateCoin
function identifyEnterKey(event){
  if (event.keyCode === 13) {
    evaluateCoin();
  }
}

// Dispense selected menu item. (See assumption 4)
function dispenseItem(selection){
  let msg = "Here is your " + selection + ". Enjoy!";
  createMsgContainer(msg);
}

// Dispense unused coins. (See assumption 4)
function dispenseCoin(dispensedCoin){
  let msg = "Unused coins returned (" + dispensedCoin + ").";
  createMsgContainer(msg);
}

// Display valid coins accepted by machine.
function displayValidCoins(){
  let msg = "Please insert 10c, 20c, 50c, $1 or $2 coin only.";
  createMsgContainer(msg);
}

// This is shown when the user enters an unrecognized input on the prompt. (see assumption 3 and 8)
function displayInvalidInput(insertedCoin) {
  let msg = "Sorry, I don't know what to do with that (Invalid coin).";
  createMsgContainer(msg);
}

// This is shown when the user inserts invalid coin 5c.
function displayInvalidCoin(insertedCoin){
  let msg = "Sorry, I can't take this coin (5c).";
  createMsgContainer(msg);
}

// Exit message
function displayExitTransaction(){
  let msg = "Bye. Until next time.";
  createMsgContainer(msg);
}

// Create message container element
function createMsgContainer(msg){
  let messageDiv = document.createElement("DIV");
  messageDiv.innerText = msg;
  messageDiv.classList.add("dispBlock");
  displayScreen.appendChild(messageDiv);
  // Automatically remove message after timeout.
  setTimeout(function(){removeDisplayMsg(messageDiv)}, timeOutVal);
}

/******************************************************************************/
// BUSINESS LOGIC
/******************************************************************************/

// Evaluate inserted coin
function evaluateCoin(event){
  let insertedCoin = document.getElementById('coinSlot').value;
  let isnertedCoinUp = insertedCoin.toUpperCase();
  let validCoin = 0;
  if (isnertedCoinUp){
    switch (isnertedCoinUp) {
      case "10C":
        validCoin = .1;
        break;
      case "20C":
        validCoin = .2;
        break;
      case "50C":
        validCoin = .5;
        break;
      case "$1":
        validCoin = 1;
        break;
      case "$2":
        validCoin = 2;
        break;
      default:
        validCoin = 0;
        hideWelcome();
        displayInvalidInput(insertedCoin);
        dispenseCoin(insertedCoin);
        displayValidCoins();
    }
  }
  totalCoin += validCoin;
  displayCoinMeter();
  clearCoinSlot();
  enableSelection()
}

// Evaluate selected menu item
function evaluateSelection(event){
  let selection = event.target.value;
  clearCoinSlot();
  switch (selection) {
    case "Organic Raw":
      if(totalCoin.toFixed(2) >= priceOrganic){
        totalCoin -= priceOrganic;
        hideWelcome();
        dispenseItem(selection);
        disableSelections();
        exitTransaction();
      }
      break;
    case "Caramel":
      if(totalCoin.toFixed(2) >= priceCaramel){
        totalCoin -= priceCaramel;
        hideWelcome();
        dispenseItem(selection);
        disableSelections();
        exitTransaction();
      }
      break;
    case "Hazelnut":
      if(totalCoin.toFixed(2) >= priceHazelnut){
        totalCoin -= priceHazelnut;
        hideWelcome();
        dispenseItem(selection);
        disableSelections();
        exitTransaction();
      }
      break;
    default:

  }
}

function enableSelection(){
  if (totalCoin.toFixed(2) >= priceOrganic){
    selOrganic.disabled = false;
  }
  if (totalCoin.toFixed(2) >= priceCaramel){
    selCaramel.disabled = false;
  }
  if (totalCoin.toFixed(2) >= priceHazelnut){
    selHazelnut.disabled = false;
  }
}

// Exit transaction
function exitTransaction() {
  // Always dispense change at the end of each transaction (see assumption 6)
  hideWelcome();
  if (totalCoin.toFixed(2) > 0) {
    dispenseCoin("$" + totalCoin.toFixed(2));
  }
  totalCoin = 0;
  clearCoinSlot()
  displayCoinMeter()
  disableSelections();
  displayExitTransaction();
}

// Debugging function
// function display(message){
//   document.getElementById("displayScreen").innerText = message;
// }
