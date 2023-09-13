import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dodument from './dodument';
import DodumentDetail from './dodument-detail';
import DodumentUpdate from './dodument-update';
import DodumentDeleteDialog from './dodument-delete-dialog';

const DodumentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Dodument />} />
    <Route path="new" element={<DodumentUpdate />} />
    <Route path=":id">
      <Route index element={<DodumentDetail />} />
      <Route path="edit" element={<DodumentUpdate />} />
      <Route path="delete" element={<DodumentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DodumentRoutes;
