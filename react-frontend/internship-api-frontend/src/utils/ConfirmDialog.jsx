import { useState } from 'react';
import './ConfirmDialog.css'; // Or use Tailwind

const ConfirmDialog = ({ title = "Are you sure?", message = "Please confirm this action.", onConfirm, onCancel }) => {
  return (
    <div className="dialog-overlay">
      <div className="dialog-box">
        <h2 className="dialog-title">{title}</h2>
        <p className="dialog-message">{message}</p>
        <div className="dialog-actions">
          <button className="dialog-button cancel" onClick={onCancel}>Cancel</button>
          <button className="dialog-button confirm" onClick={onConfirm}>Confirm</button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmDialog;