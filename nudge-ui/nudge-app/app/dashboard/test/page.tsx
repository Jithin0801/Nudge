"use client";

import { useState } from "react";

// Child component - displays ONE account's info
function AccountCard({ accountName, balance }) {
  // TODO: Display the account name and balance
  return (
    <div style={{ border: "1px solid black", padding: "10px", margin: "5px" }}>
      <h3>{accountName}: </h3>{" "}
      <h3>
        <strong>{balance}</strong>
      </h3>
    </div>
  );
}

function ActiveAccountCard({ accountName, balance }) {
  // TODO: Display the account name and balance
  return (
    <div style={{ border: "1px solid black", padding: "10px", margin: "5px" }}>
      <h3>{accountName}: </h3>{" "}
      <h3>
        <strong>{balance}</strong>
      </h3>
    </div>
  );
}

export default function BankingDashboard() {
  const savingAcc = "savings";
  const [savingbal, setSavingBal] = useState(1000);

  const checkingAcc = "checking";
  const [checkingbal, setCheckingBal] = useState(2000);

  const investmentAcc = "investment";
  const [investmentbal, setinvestmentBal] = useState(5000);

  const [transCount, setTransCount] = useState(0);

  const [activeAcc, setActiveAcc] = useState("No acount selected");

  // TODO: Create state for:
  // - Currently selected account (start with "savings")
  // - Balance for savings account (start with 1000)
  // - Balance for checking account (start with 2000)
  // - Balance for investment account (start with 5000)
  // - Total transaction count (start with 0)

  function handleDeposit() {
    // TODO: Add 100 to the CURRENT account's balance
    // TODO: Increment transaction count

    if (activeAcc === "savings") {
      setSavingBal(savingbal + 100);
    }
    if (activeAcc === "checking") {
      setCheckingBal(checkingbal + 100);
    }
    if (activeAcc === "investment") {
      setinvestmentBal(investmentbal + 100);
    }
    setTransCount(transCount + 1);
  }

  function handleWithdraw() {
    // TODO: Subtract 50 from the CURRENT account's balance
    // TODO: Increment transaction count

    if (activeAcc === "savings") {
      setSavingBal(savingbal - 50);
    }
    if (activeAcc === "checking") {
      setCheckingBal(checkingbal - 50);
    }
    if (activeAcc === "investment") {
      setinvestmentBal(investmentbal - 50);
    }
    setTransCount(transCount + 1);
  }

  function handleAccountChange(e) {
    setActiveAcc(e.target.value);
  }

  return (
    <div>
      <h1>Banking Dashboard</h1>
      {/* TODO: Dropdown to select account */}
      <select onChange={handleAccountChange}>
        <option value="" selected disabled hidden>
          Choose an option
        </option>
        <option value="savings">Savings Account</option>
        <option value="checking">Checking Account</option>
        <option value="investment">Investment Account</option>
      </select>
      <p>
        Transaction Count: <strong>{transCount}</strong>
      </p>
      {/* TODO: Display current account and its balance */}
      <h4>Selected Account: </h4>{" "}
      <ActiveAccountCard
        accountName={activeAcc}
        balance={
          activeAcc === "savings" ? savingbal
          : activeAcc === "investment" ?
            investmentbal
          : activeAcc === "checking" ?
            checkingbal
          : 0
        }
      />
      {/* TODO: Deposit and Withdraw buttons */}
      <button onClick={handleDeposit}>Deposit 100$</button>
      <button onClick={handleWithdraw}>Withdraw -50$</button>
      {/* TODO: Display transaction count */}
      <h3>All Accounts Summary:</h3>
      <AccountCard accountName={savingAcc} balance={savingbal} />
      <AccountCard accountName={checkingAcc} balance={checkingbal} />
      <AccountCard accountName={investmentAcc} balance={investmentbal} />
    </div>
  );
}
